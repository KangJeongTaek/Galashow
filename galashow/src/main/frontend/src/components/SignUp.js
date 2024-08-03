import React, { useState } from 'react';
import { Button, Container } from 'react-bootstrap';
import axios from 'axios';
const containsWhiespace = (str) => /\s/.test(str); 

const SignUp = () => {

    const [member,setMember] = useState({
        memberid : '',
        memberpassword : '',
        nickName : ''
    })
    const idChange = (e) =>{
        const {value} = e.target;
        setMember(prevmember =>({
            ...prevmember,
            memberid : value
        }))
    }

    const passwordChange = (e) =>{
        const {value} = e.target;
        setMember(prevmember =>({
            ...prevmember,
            memberpassword : value
        }))
    }
    const nickNameChange = (e) =>{
        const {value} = e.target;
        setMember(prevmember => ({
            ...prevmember,
            nickName : value
        }))
    }

    const signUp = () => {
        if(member.memberid === ''){
            alert("아이디를 입력해주세요.");
            return
        }
        if(member.memberpassword === ''){
            alert('비밀번호를 입력해주세요');
            return
        }
        if(containsWhiespace(member.memberid) || containsWhiespace(member.memberpassword)){
            alert("아이디와 비밀번호는 공백을 포함할 수 없습니다.");
            return
        }

        axios.post("http://localhost:8080/member/join",member)
        .then((response) => console.log(response.status))
        .catch((response) => console.log(response))

    } 

    return (
        <div>
            <Container>
            <div className='m-3'>
                <label htmlFor='id' className='form-label'>아이디</label>
                <input type='text' className='form-control' id='id' aria-describedby='idhelp' onChange={idChange}/>
                <div id='idhelp' className='form-text mb-4'>아이디를 입력해주세요</div>

                <label htmlFor='password'>비밀번호</label>
                <input type='password' className='form-control' id='password' aria-describedby='passwordhelp' onChange={passwordChange}/>
                <div id='passwordhelp' className='form-text mb-4'>비밀번호를 입력해주세요</div>

                <label htmlFor='nickName'>별명</label>
                <input type='text' className='form-control' id='nickName' aria-describedby='nickNamehelp' onChange={nickNameChange}/>
                <div id='nickNamehelp' className='form-text'>사이트 내에서 사용할 별명을 작성해주세요</div>
            </div>
            <Button onClick={signUp}>
                회원가입
            </Button>
            </Container>
        </div>
    );
};

export default SignUp;