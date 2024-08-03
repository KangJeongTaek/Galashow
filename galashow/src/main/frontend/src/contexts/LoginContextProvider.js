import React, { createContext, useState } from 'react';
import api from '../apis/api';
import * as auth from "../apis/auth.js";
import Cookies from 'js-cookie';
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


    // 로그인 체크
    // 쿠키에 jwt가 있는지 확인
    // jwt로 사용자 정보를 요청
    const loginCheck = async () => {
        // 토큰을 가지고 서버에게 요청 보내기
        let response 
        let data
        response = await auth.info();
    }

    //  로그인 요칭
    const login = async (memberId,password) => {
        console.log(memberId);
        console.log(password);

        const response = await auth.login(memberId,password);
        const data = response.data
        const status = response.status;
        const headers = response.headers;
        const authorization = headers.authorization;
        const accessToken = authorization.replace("Bearer ","");

        console.log(data);
        console.log(status);
        console.log(headers);
        console.log(accessToken);

        // 로그인 성공
         if(status === 200){
            //로그인 체크 (/member/{memberId})

            alert('로그인 성공!')
         }
    }



    // 로그인 세팅
    //memberData, accessToken(jwt)
    const loginSetting = (memberData,accessToken) => {
        const {mid,memberId,auth} = memberData;

        // axios 객체의 hedaer(Authorizitaion : `Bearer ${accessToken}`)
        api.defaults.headers.common.Authorization = `Bearer ${accessToken}`

        // 쿠키에 accessToken(jwt) 저장
        Cookies.set("accesToken",accessToken);

        // 로그인 여부 : true
        setLogin(true);
        // 유저 정보 세팅
        const updatedMemberInfo = {mid, memberId,auth}

        setMemberInfo(updatedMemberInfo);

        // 권한 정보 세팅
        const updatedRoles =  {isUser : false,isAdmin : false};
        if(auth === 'ROLE_USER')  updatedRoles.isUser = true;
        if(auth === 'ROLE_ADMIN') updatedRoles.isAdmin = true;
        setRoles(updatedRoles);
    }

    // 로그아웃 세팅
    const logoutSettion = () =>{
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

    const logout = () =>{
        setLogin(false)
    }

    return (
        <LoginContext.Provider value={{isLogin,logout}}>
                {children}
            </LoginContext.Provider>
    );
};

export default LoginContextProvider;