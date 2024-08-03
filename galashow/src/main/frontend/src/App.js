import './App.css';
import {Route,Routes} from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/js/bootstrap.min.js';
import Home from './components/Home';
import Layout from './components/Layout';
import SignUp
 from './components/SignUp';
import LoginContextProvider from './contexts/LoginContextProvider';
function App() {
  return (
    <LoginContextProvider>
      <Routes>
        <Route element={<Layout/>}>
          <Route index element={<Home/>}/>
          <Route path='/SignUp' element={<SignUp/>}/>
        </Route>
      </Routes>
    </LoginContextProvider>

  );
}

export default App;
