import React from "react";

const noImageUrl = 'https://orbis-alliance.com/wp-content/themes/consultix/images/no-image-found-360x260.png';

// returns a badge with a color based on the status
const StatusBadge = ({ status }) => {
    let color = '';
    switch (status) {
        case 'full':
            color = 'bg-info'; // red for full
            break;
        case 'ended':
            color = 'bg-gray-400'; // gray for ended
            break;
        default:
            // default color or handle other statuses like "x/y capacity"
            color = 'bg-success'; // green for available
    }
    
    return <span className={`w-fit text-xs text-base-100 py-1 px-2 rounded-full ${color}`}>{status.toUpperCase()}</span>;
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
            {imageSrc && <img 
                className="w-full rounded-lg object-cover aspect-[9/8]" // Remove h-auto and use h-full to fill the container
                src={`data:image/jpeg;base64,${imageSrc}`}
                alt={name} 
            />}
            {!imageSrc && <img 
                className="w-full rounded-lg object-cover aspect-[9/8]" // Remove h-auto and use h-full to fill the container
                src={noImageUrl}
                alt={name} 
            />}
            
            <div className="py-4">
            <div className="flex justify-between items-center font-semibold">
                <div className="text-primary-500 overflow-hidden overflow-ellipsis whitespace-nowrap">
                    {name}
                </div>
                <div className="text-right">
                    <StatusBadge status={status} />
                </div>
            </div>

                <div className="py-1" />

                {/* description */}
                <p className="text-primary-500 text-base overflow-hidden overflow-ellipsis whitespace-nowrap max-w-full">{description}</p>

                <div className="py-1" />

                {/* date, time, length, instructor */}
                <div className="text-primary-100 text-sm">
                    <p>{date} - {time} - {lengthInHrs}hrs</p>
                    <p>{instructor}</p>
                </div>
            </div>
        </div>
    );
};
  
  export default ClassListItem;