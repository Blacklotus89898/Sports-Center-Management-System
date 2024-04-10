import { BrowserRouter, Routes, Route } from 'react-router-dom'

import Home from './pages/Home'
import Temp from './pages/Temp'
import NoPage from './pages/NoPage'
import ThemeProvider from './providers/ThemeProvider'
import Themes from './pages/Themes'
import Search from './pages/Search'
import Demo from './components/SchedulerComponents/Scheduler'
import Dashboard from './pages/Dashboard'
import Calendar from './components/SchedulerComponents/Calender'
import AboutUs from './pages/AboutUs'
import Career from './pages/Career'
import ContactUs from './pages/ContactUs'
import AboutSystem from './pages/AboutSystem'

function App() {
  return (
    <BrowserRouter>
        <ThemeProvider >
            <Routes>
                <Route index element={<Search />} />
                <Route path="/temp" element={<Temp />} />
                <Route path="/about-us" element={<AboutUs />} />
                <Route path="/careers" element={<Career />} />
                <Route path="/contact" element={<ContactUs />} />
                <Route path="/about-system" element={<AboutSystem />} />
                <Route path="/themes" element={<Themes />} />
                <Route path="/search" element={<Search />} />
                <Route path="/schedule" element={<Demo />} />
                <Route path="/dashboard" element={<Dashboard />} />
                <Route path="/dashboard/:path" element={<Dashboard />} />
                <Route path="*" element={<NoPage />} />
                <Route path="/calendar" element={<Calendar />} />
 
            </Routes>
        </ThemeProvider>
    </BrowserRouter>
  )
}

export default App;
