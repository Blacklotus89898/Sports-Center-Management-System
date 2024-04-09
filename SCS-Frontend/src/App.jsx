import { BrowserRouter, Routes, Route } from 'react-router-dom'

import Home from './pages/Home'
import Temp from './pages/Temp'
import NoPage from './pages/NoPage'
import ThemeProvider from './providers/ThemeProvider'
import Themes from './pages/Themes'
import Search from './pages/Search'
import Demo from './components/SchedulerComponents/Calender'
import Dashboard from './pages/Dashboard'
import Calendar from './components/SchedulerComponents/Calender'

function App() {
  return (
    <BrowserRouter>
        <ThemeProvider >
            <Routes>
                <Route index element={<Search />} />
                <Route path="/temp" element={<Temp />} />
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
