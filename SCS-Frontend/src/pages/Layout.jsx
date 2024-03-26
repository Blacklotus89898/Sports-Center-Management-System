import { Outlet, Link } from "react-router-dom";
import Navbar from "../components/Navbar";

export default function Layout() {
  return (
    <>
      {/* <nav>
        <ul>
          <li>
            <Link to="/">Home</Link>
          </li>
          <li>
            <Link to="/temp">Temp</Link>
          </li>
        </ul>
      </nav> */}

      <Navbar /> {/* Added Navbar component */}

      <Outlet />
    </>
  )
};