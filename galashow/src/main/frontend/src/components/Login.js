import React, { useContext} from 'react';
import { Container } from 'react-bootstrap';
import { LoginContext } from '../contexts/LoginContextProvider';
const Login = () => {

    const {login} = useContext(LoginContext);

    const onLogin = (e) =>{
        e.preventDefault();
        const form = e.target;
        const memberId = form.memberId.value;
        const password = form.password.value;
        console.log(memberId);
        login(memberId,password);
    }

    return (
        <div>
        <Container>
        <div className='m-3'>
            <form onSubmit={(e) => onLogin(e)}>
                <label htmlFor='id' className='form-label'>아이디</label>
                <input type='text' className='form-control' id='id' aria-describedby='idhelp' name='memberId' placeholder='아이디' required/>
                <div id='idhelp' className='form-text mb-4'>아이디를 입력해주세요</div>

                <label htmlFor='password'>비밀번호</label>
                <input type='password' className='form-control' id='password' aria-describedby='passwordhelp' name='password' placeholder='비밀번호' required/>
                <div id='passwordhelp' className='form-text mb-4'>비밀번호를 입력해주세요</div>
                <button type='submit' className='btn btn-sm btn-primary'>
                    로그인
                </button>
            </form>

        </div>

        </Container>
    </div>
    );
};

export default Login;