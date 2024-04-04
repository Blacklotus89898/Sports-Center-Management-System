import { BrowserRouter, Routes, Route } from 'react-router-dom'

import Home from './pages/Home'
import Temp from './pages/Temp'
import NoPage from './pages/NoPage'
import ThemeProvider from './providers/ThemeProvider'
import Themes from './pages/Themes'
import Search from './pages/Search'
import Profile from './pages/Profile'

function App() {
  return (
    <BrowserRouter>
        <ThemeProvider >
            <Routes>
                <Route index element={<Home />} />
                <Route path="/temp" element={<Temp />} />
                <Route path="/themes" element={<Themes />} />
                <Route path="/search" element={<Search />} />
                <Route path="/profile" element={<Profile />} />
                <Route path="*" element={<NoPage />} />
            </Routes>
        </ThemeProvider>
    </BrowserRouter>
  )
}

export default App
