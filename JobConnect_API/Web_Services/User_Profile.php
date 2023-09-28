<?php

require_once '../Data_operations/User_Operation.php';


$response = array();

if ($_SERVER["REQUEST_METHOD"] == "POST") {
    $uName = isset($_POST['uName']) ? $_POST['uName'] : '';
    $uEmail = isset($_POST['uEmail']) ? $_POST['uEmail'] : '';
    $uDob = isset($_POST['uDob']) ? $_POST['uDob'] : '';
    $uImg = isset($_POST['uImg']) ? $_POST['uImg'] : '';
    $uEducation = isset($_POST['uEducation']) ? $_POST['uEducation'] : '';
    $uBio = isset($_POST['uBio']) ? $_POST['uBio'] : '';  
    $uCv = isset($_POST['uCv']) ? $_POST['uCv'] : '';  
    $uPhone = isset($_POST['uPhone']) ? $_POST['uPhone'] : '';    
    $uCategories = isset($_POST['uCategories']) ? $_POST['uCategories'] : '';
    $uIndustry = isset($_POST['uIndustry']) ? $_POST['uIndustry'] : '';
   

    if (!empty($uName) && !empty($uEmail) && !empty($uDob) && !empty($uEducation) && !empty($uImg) && !empty($uCategories) && !empty($uBio) && !empty($uCv) && !empty($uPhone)) {
        $pathImg='../Images/'. date("d-m-y") . '-' . time() . '-' . rand(10000,1000000) . '.jpeg';
      

        if((file_put_contents($pathImg,base64_decode($uImg)))){
            $db = new User();
            $userProfile = $db->userProfile($uName, $uEmail, $uDob,$uPhone, $uEducation,$uBio, $pathImg,$uCv, $uCategories,$uIndustry);
    
            if ($userProfile == 1) {
                $update_reg_complete=$db->update_reg_complete($uEmail);
                  if($update_reg_complete==1){
                    $response['error'] = false;
                    $response['message'] = "User profile saved successfully";
                  }else{
                    $response['error'] = true;
                    $response['message'] = "Failed to update user profile. Please try again.";
                  }          
            } else if ($userProfile == 2) {
                $response['error'] = true;
                $response['message'] = "Failed to save user profile. Please try again.";
            } else if ($userProfile == 0){
                $response['error'] = true;
                $response['message'] = "Already registered";
            }else {
                $response['error'] = true;
                $response['message'] = "An error occurred. Please try again.";
            }
        }else{
            $response['error'] = true;
            $response['message'] = "Plese upload necessary File";
        }
     
    } else {
        $response['error'] = true;
        $response['message'] = "One or more fields are empty. Please fill all the fields and try again.";
    }
} else {
    $response['error'] = true;
    $response['message'] = "Invalid request method";
}

echo json_encode($response);
