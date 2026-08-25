import React from 'react'

function Input({placeholder,text,onChange}){
    return(
        <input className='bg-gray-800' placeholder={placeholder} value={text} onChange={onChange} />
    )
}

export f