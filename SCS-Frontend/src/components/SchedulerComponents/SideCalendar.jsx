import { useState } from 'react';
import Calendar from 'react-calendar';
import 'react-calendar/dist/Calendar.css';

function App() {
  const [date, setDate] = useState(new Date());

  return (
    <div className="flex flex-col items-center justify-center h-screen">
      <h1 className="text-3xl font-bold mb-4">React Calendar</h1>
      <div className="bg-white p-4 rounded shadow-lg">
        <Calendar onChange={setDate} value={date} />
      </div>
      <p className="mt-4">
        <span className="font-bold">Selected Date:</span> {date.toDateString()}
      </p>
    </div>
  );
}

export default App;