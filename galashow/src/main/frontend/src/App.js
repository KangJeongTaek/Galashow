import './App.css';
import {Route,Routes} from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/js/bootstrap.min.js';
import Home from './components/Home';
import Layout from './components/Layout/Layout';
import Join from './components/Join';
import LoginContextProvider from './contexts/LoginContextProvider';
import Login from './components/Login';
import Mypage from './components/Mypage';
function App() {
  return (
    <LoginContextProvider>
      <Routes>
        <Route element={<Layout/>}>
          <Route index element={<Home/>}/>
          <Route path='/login' element={<Login/>}/>
          <Route path='/join' element={<Join/>}/>
          <Route path='/user' element={<Mypage/>}/>
        </Route>
      </Routes>
    </LoginContextProvider>

  );
}

export default App;
