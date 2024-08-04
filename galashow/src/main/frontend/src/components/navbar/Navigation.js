
import React, { useContext } from 'react';
import {Navbar,Nav, Container,Button} from 'react-bootstrap'
import { LoginContext } from '../../contexts/LoginContextProvider';
import { Link } from 'react-router-dom';
const Navigation = () => {
    const {isLogin,logout} = useContext(LoginContext);
    return (
        <Navbar bg='primary'>
            <Container>
                <Navbar.Brand href='/'>GalaShow</Navbar.Brand>
                {
                    !isLogin ?
                    // 로그인 시
                    <Nav className='me-auto'>
                        <Link to='/' className='nav-link'>Home</Link>
                        <Link to='/login' className='nav-link'>로그인</Link>
                        <Link to='/join' className='nav-link'>회원가입</Link>
                    </Nav> :
                    // 비로그인시
                    <Nav className='me-auto'>
                        <Link to='/' className='nav-link'>Home</Link>
                        <Link to='/user' className='nav-link'>마이페이지</Link>
                        <Button className='btn-sm btn-secondary' onClick={() => logout()}>로그아웃</Button>
                </Nav>
                }
            </Container>
        </Navbar>
    );
};

export default Navigation;