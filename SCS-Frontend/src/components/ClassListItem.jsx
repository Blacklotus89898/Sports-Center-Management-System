import React from "react";

// returns a badge with a color based on the status
const StatusBadge = ({ status }) => {
    let color = '';
    switch (status) {
        case 'full':
            color = 'bg-red-500'; // red for full
            break;
        case 'ended':
            color = 'bg-gray-400'; // gray for ended
            break;
        default:
            // default color or handle other statuses like "x/y capacity"
            color = 'bg-green-500'; // green for available
    }
    
    return <span className={`text-xs text-white py-1 px-2 rounded-full ${color}`}>{status.toUpperCase()}</span>;
};

//  displays
const ClassListItem = ({
    imageSrc,
    status,
    name,
    description,
    date,
    time,
    lengthInHrs,
    instructor
}) => {
    return (
        <div className="flex flex-col bg-base-100 hover:cursor-pointer">
            {/* image */}
            <img 
                className="w-full rounded-lg object-cover aspect-[9/8]" // Remove h-auto and use h-full to fill the container
                src={imageSrc} 
                alt={name} 
            />
            
            <div className="py-4">
                <div className="flex flex-row font-semibold justify-between overflow-ellipsis whitespace-nowrap items-center">
                    {name}
                    <StatusBadge status={status} />
                </div>

                {/* description */}
                <p className="text-gray-700 text-base overflow-hidden overflow-ellipsis whitespace-nowrap max-w-full">{description}</p>

                <div className="py-1" />

                {/* date, time, length, instructor */}
                <div className="text-gray-600 text-sm">
                    <p>{date} - {time} - {lengthInHrs}hrs</p>
                    <p>{instructor}</p>
                </div>
            </div>
        </div>
    );
};
  
  export default ClassListItem;