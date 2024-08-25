import React, { createContext, useState } from 'react';
import api from '../apis/api';
import * as auth from "../apis/auth.js";
import Cookies from 'js-cookie';
import { useNavigate } from 'react-router-dom';
export const LoginContext = createContext();

// 로그인 체크
// 로그인
// 로그아웃

// 로그인 세팅
// 로그아웃 세팅

const LoginContextProvider = ({children}) => {
    /* 상태
        - 로그인 여부
        - 유저 정보
        - 권한 정보
        - 아이디 저장
    */
    // context value : 로그인 여부, 로그아웃 함수
    const [isLogin,setLogin] = useState(false);

    // 유저 정보 
    const [memberInfo,setMemberInfo] = useState({});

    // 권한 정보
    const [roles,setRoles] = useState({isUser : false,isAdmin : false});

    // 아이디 저장
    const [rememberUserId,setRememberUserId] = useState();

    // 페이지 이동
    const navigate = useNavigate();

    // 로그인 체크
    // 쿠키에 jwt가 있는지 확인
    // jwt로 사용자 정보를 요청
    const loginCheck = async () => {
        //  쿠키에서 토큰 가져오기
        const accessToken = Cookies.get("accessToken");
        console.log(`accessToken : ${accessToken}`);
        
        // accessToken 없음
        if(!accessToken){
            console.log(`쿠키에 accessToken(JWT)이  없음`);
            logoutSetting();
            return;
        }
        // header에 jwt 담기
        // accessToken 있음
        api.defaults.headers.common.Authorization = `Bearer ${accessToken}`;

        // 토큰을 가지고 서버에게 요청 보내기
        let data
        auth.info().then((response) =>{
            data = response.data;
            console.log(`data : ${data}`);
            console.log(data);

            //  인증 실패
            if(data === 'UNAUTHORIZED' || response.status === 401){
                console.error("accessToken (jwt)가 민료되거나 인증에 실패했습니다.");
                return;
            }


            loginSetting(data,accessToken);
            navigate("/");
        }).catch(error => {
            console.log(`status : ${error.response.status}`);
            return;
        });
    }

    //  로그인 요칭
    const login = async (memberId,password) => {
        console.log(memberId);
        console.log(password);

            auth.login(memberId,password)
            .then(response => {
                const data = response.data;
                const status = response.status;
                const headers = response.headers;
                const authorization = headers.authorization;
                const accessToken = authorization.replace("Bearer ","");
                console.log(data);
                console.log(status);
                console.log(headers);
                console.log(accessToken);
                if(response.status === 200){
                    // 쿠키에 accessToken(jwt) 저장
                    Cookies.set("accessToken",accessToken);

                    // 로그인 체크 (/member/info <---- memberData)
                    loginCheck();
                    alert("로그인 성공!!");
                    navigate("/");
                }
            }).catch(error => {
                alert("해당하는 회원 정보가 없습니다. 다시 한 번 확인해주세요.");
            });  
    }



    // 로그인 세팅
    //memberData, accessToken(jwt)
    const loginSetting = (memberData,accessToken) => {
        const {mid,memberId,authority} = memberData;

        console.log(`mid : ${mid}`);
        console.log(`memberId : ${memberId}`);
        console.log(`authority : ${authority}`);
        // axios 객체의 hedaer(Authorizitaion : `Bearer ${accessToken}`)
        api.defaults.headers.common.Authorization = `Bearer ${accessToken}`


        // 로그인 여부 : true
        setLogin(true);
        // 유저 정보 세팅
        const updatedMemberInfo = {mid, memberId,auth}

        setMemberInfo(updatedMemberInfo);

        // 권한 정보 세팅
        const updatedRoles =  {isUser : false,isAdmin : false};
        if(authority === 'ROLE_USER')  updatedRoles.isUser = true;
        if(authority === 'ROLE_ADMIN') updatedRoles.isAdmin = true;
        setRoles(updatedRoles);
    }

    // 로그아웃 세팅
    const logoutSetting = () =>{
        //axios 헤더 초기화
        api.defaults.headers.common.Authorization = undefined;
        //쿠키 초기화
        Cookies.remove("accessToken")
        //로그인 여부 : false
        setLogin(false)
        // 유저 정보 초기화
        setMemberInfo(null)
        // 권한 정보 초기화
        setRoles(null)
    }

    // 로그아웃 처리
    const logout = () =>{
        if(window.confirm("로그아웃 하시겠습니까?")){
            logoutSetting();

            // 메인 페이지 이동
            navigate("/");
        }
        
    }

    return (
        <LoginContext.Provider value={{isLogin,logout,login,memberInfo,roles}}>
                {children}
            </LoginContext.Provider>
    );
};

export default LoginContextProvider;