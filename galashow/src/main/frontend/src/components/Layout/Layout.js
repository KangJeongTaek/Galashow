import React from 'react';
import Navigation from '../navbar/Navigation';
import { Outlet } from 'react-router-dom';

const Layout = () => {
    return (
        <div>
            <Navigation/>
            <main>
                <Outlet/>
            </main>
        </div>
    );
};

export default Layout;