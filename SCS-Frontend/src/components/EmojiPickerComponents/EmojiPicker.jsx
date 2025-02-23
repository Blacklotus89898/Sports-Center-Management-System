import React, { useState } from 'react';

import { emojis } from './Emojis';

// export const emojis = [
//     { emoji: '⚽️', name: 'Soccer' },


export default function EmojiPicker({ selectedEmoji, setSelectedEmoji, col = false }) {
    const [search, setSearch] = useState('');

    return (
        <div className={`flex ${col ? 'flex-col' : 'flex-row'} h-fit justify-center items-center`}>
            <div className="flex w-full text-6xl justify-center items-center">
                {/* displays the picked emoji */}
                <div id="emoji">{selectedEmoji ? selectedEmoji : '?'}</div>
            </div>
            <div className="flex flex-col w-full">
                {/* search input */}
                <input
                    type="text"
                    value={search}
                    onChange={(e) => setSearch(e.target.value)}
                    placeholder="Search for an icon..."
                    className="input w-full my-2"
                />

                {/* emoji list to pick from */}
                <div className="flex flex-wrap w-full h-28 overflow-y-auto">
                    {emojis
                        .filter((emoji) => emoji.name.includes(search))
                        .map((emoji) => (
                            <button
                                key={emoji.emoji}
                                onClick={() => setSelectedEmoji(emoji.emoji)}
                                className="m-1 text-2xl"
                            >
                                {emoji.emoji}
                            </button>
                        ))}
                </div>
            </div>
        </div>
    );
}