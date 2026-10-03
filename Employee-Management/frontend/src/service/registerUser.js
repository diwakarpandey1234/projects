import api from "./api";

export const registerUser = async (formData) => {
  console.log("Sending payload:", formData); 

  const response = await api.post(
    "/api/user/register",
    formData, 
    {
      headers: {
        "Content-Type": "application/json"
      }
    }
  );
    return response.data;
};

export const registerHR=async (formData)=>{
    const response = await api.post(
    "/api/user/register",
    formData, 
    {
      headers: {
        "Content-Type": "application/json"
      }
    }
  );
    return response.data;
}