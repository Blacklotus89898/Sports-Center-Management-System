import { useState } from 'react'
import { BrowserRouter, Routes, Route } from 'react-router-dom'

import Layout from './pages/Layout'
import Home from './pages/Home'
import Temp from './pages/Temp'
import NoPage from './pages/NoPage'
import Sandbox from './pages/Sandbox'

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Layout />}>
          <Route index element={<Home />} />
          <Route path="/temp" element={<Temp />} />
          <Route path="/Sandbox" element={<Sandbox />} />
          <Route path="*" element={<NoPage />} />
        </Route>
      </Routes>
    </BrowserRouter>
  )
}

export default App
