import React, { useState } from 'react';
import { Button, Container } from 'react-bootstrap';
import axios from 'axios';
import * as auth from '../apis/auth';
import { join } from '../services/join';
import { useNavigate } from 'react-router-dom';
const containsWhiespace = (str) => /\s/.test(str); 

const SignUp = () => {

    const navigate = useNavigate();

    const onJoin = async (e) =>{
        e.preventDefault();
        console.log(e.target);
        const form = e.target;
        const memberId = form.memberId.value;
        const password = form.password.value;
        const nickName = form.nickName.value;

        if(memberId === null || memberId === ''){
            alert('아이디를 입력해주세요.');
            return;
        }
        if(password === null || password === ''){

         alert('비밀번호를 입력해주세요.');
          return;
        }
        if(nickName === null || nickName === ''){
         alert('닉네임을 입력해주세요,');
         return;
        }
        
        //회원가입 APi 보내기
        const status = await join({memberId,password,nickName});
        console.log(status);
        if(status === 201){
            navigate("/login");
        }
    }

    return (
        <div>
            <Container>
            <div className='m-3'>
            <form onSubmit={onJoin}>
                <label htmlFor='id' className='form-label'>아이디</label>
                <input type='text' className='form-control' id='id' aria-describedby='idhelp' name='memberId' placeholder='아이디' autoComplete='memberId' required/>
                <div id='idhelp' className='form-text mb-4'>아이디를 입력해주세요</div>

                <label htmlFor='password'>비밀번호</label>
                <input type='password' className='form-control' id='password' aria-describedby='passwordhelp' name='password' placeholder='비밀번호' autoComplete='password' required/>
                <div id='passwordhelp' className='form-text mb-4'>비밀번호를 입력해주세요</div>

                <label htmlFor='nickName'>별명</label>
                <input type='text' className='form-control' id='nickName' aria-describedby='nickNamehelp' name='nickName' placeholder='별명' autoComplete='nickName' required/>
                <div id='nickNamehelp' className='form-text mb-4'>홈페이지에서 사용할 별명을 입력해주세요</div>

                <Button type='submit'>
                    회원가입
                </Button>
            </form>
            </div>
            </Container>
        </div>
    );
};

export default SignUp;