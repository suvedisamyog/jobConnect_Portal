<?php

require_once '../Data_operations/Org_Operation.php';
$response = array();

if ($_SERVER["REQUEST_METHOD"] == "POST"){
    $oName = isset($_POST['oName']) ? $_POST['oName'] : '';
    $oEmail = isset($_POST['oEmail']) ? $_POST['oEmail'] : '';
    $oPhone = isset($_POST['oPhone']) ? $_POST['oPhone'] : '';   
    $oImg = isset($_POST['oImg']) ? $_POST['oImg'] : '';
    $oWeb = isset($_POST['oWeb']) ? $_POST['oWeb'] : '';
    $oLocation = isset($_POST['oLocation']) ? $_POST['oLocation'] : '';

    if(!empty($oName) && !empty($oEmail) && !empty($oPhone) && !empty($oImg) && !empty($oImg) && !empty($oLocation)){
        $pathImg='../Images/'. date("d-m-y") . '-' . time() . '-' . rand(10000,1000000) . '.jpeg';
        if(file_put_contents($pathImg,base64_decode($oImg))){
            $db=new Org();
            $orgprofile = $db->orgprofile($oName, $oEmail,$oPhone, $pathImg,$oWeb ,$oLocation);
            if ($orgprofile == 1) {
                $update_reg_complete=$db->update_reg_complete($oEmail);
                  if($update_reg_complete==1){
                    $response['error'] = false;
                    $response['message'] = "User profile saved successfully";
                  }else{
                    $response['error'] = true;
                    $response['message'] = "Failed to update user profile. Please try again.";
                  }          
            } else if ($orgprofile == 2) {
                $response['error'] = true;
                $response['message'] = "Failed to save user profile. Please try again.";
            } else if ($orgprofile == 0){
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

    }else{
        $response['error'] = true;
        $response['message'] = "One or more fields are empty. Please fill all the fields and try again.";
    }

} else {
    $response['error'] = true;
    $response['message'] = "Invalid request method";
}

echo json_encode($response);
