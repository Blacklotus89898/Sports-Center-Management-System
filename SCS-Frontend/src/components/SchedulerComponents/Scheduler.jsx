import React from 'react';
import Calendar from './Calendar'; // 导入日历组件
import { useState } from 'react';
import SideCalendar from './SideCalendar'
import { createSchedulerAction } from './Calendar';

import { PageProvider } from "../../providers/PageProvider";


const App = () => {

  const [selectedDate, setSelectedDate] = useState(null);

const handleDateSelect = (date) => {
  store.dispatch(createSchedulerAction('currentDate', date));
};

  return (
    <PageProvider>
      <style>
        {`
          @media (max-width: 1300px) {
            .hide-on-small {
              display: none;
            }
            .expand-on-small {
              width: 100% 
            }
          }
        `}
      </style>
      <div className="flex justify-center items-center  bg-transparent">
      <div className="flex space-x-0.5 w-full my-8"> {/* 调整宽度为 w-full */}
        {/* 左侧深色区域 */}
        <div className="w-1/3 bg-[#282828] text-white p-6 rounded-lg hide-on-small ">
        <h2 className="text-2xl font-semibold mb-4">我的日历</h2>
        <div className="flex items-center justify-center mb-6 w-full">
        <SideCalendar onDateSelect={handleDateSelect} />       
        </div>
      </div>
        
        {/* 右侧浅色区域 */}
        <div className={`w-2/3 bg-[#EFEFEF] p-6 rounded-lg shadow-lg expand-on-small`}>
          {/* 右侧浅色区域内容 */}
          <div className="flex items-center justify-between mb-6">
            <h1 className="text-3xl font-semibold">日历</h1>
            <button className="bg-[#b591ef] text-white px-4 py-2 rounded">添加事件</button>
          </div>
          
          {/* 日历组件 */}
          <Calendar selectedDate={selectedDate} />
        </div>
      </div>
    </div>
    </PageProvider>
  );
};

export default App;