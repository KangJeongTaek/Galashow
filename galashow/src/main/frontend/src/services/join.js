import { Navigate, useNavigate } from "react-router-dom";
import * as auth from "../apis/auth"

export const join = async(form) =>{
    let response;
    let data;
    console.log(form);
    try{
        response = await auth.join(form);
    }catch(error){
        console.error(`${error}`);
        console.error(`회원가입 요청 중 에러가 발생했습니다.`);
        if(error.response.status === 409){
            alert(`${error.response.data}`);
        }
        return;
    }

    data = response.data;
    const status = response.status;

    console.log(`data : ${data}`);
    console.log(`status : ${status}`);

    if(status === 201){
        console.log(`회원가입 성공!!`);
        alert("회원가입에 성공하셨습니다.");
    }else{
        alert("회원가입에 실패했습니다.");
    }

    return status;
}