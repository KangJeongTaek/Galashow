import React from 'react';
import {Navbar,Nav, Container} from 'react-bootstrap'
const Navigation = () => {
    return (
        <Navbar bg='primary'>
            <Container>
                <Navbar.Brand href='#home'>GalaShow</Navbar.Brand>
                <Nav className='me-auto'>
                    <Nav.Link href='/'>Home</Nav.Link>
                    <Nav.Link href='/SignUp'>SignUp</Nav.Link>
                </Nav>
            </Container>
        </Navbar>
    );
};

export default Navigation;