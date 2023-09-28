<?php

require_once '../Data_operations/Org_Operation.php';
$response = array(); 
if($_SERVER['REQUEST_METHOD']=='POST'){

    $oName = isset($_POST['name']) ? $_POST['name'] : '';
    $oEmail = isset($_POST['email']) ? $_POST['email'] : '';

    $oPhone = isset($_POST['phone']) ? $_POST['phone'] : '';   
    $oImg = isset($_POST['image']) ? $_POST['image'] : '';
    $oWeb = isset($_POST['website']) ? $_POST['website'] : '';
    $oLocation = isset($_POST['location']) ? $_POST['location'] : '';
    if(!empty($oName) &&   !empty($oPhone) && !empty($oWeb)&& !empty($oImg) && !empty($oLocation)){
        $pathImg='../Images/'. date("d-m-y") . '-' . time() . '-' . rand(10000,1000000) . '.jpeg';
        if(file_put_contents($pathImg,base64_decode($oImg))){
            $db=new Org();
            $updateprofile=$db->orgprofileUpdate($oName,$oEmail,$oPhone,$oWeb,$pathImg, $oLocation);
            if($updateprofile){
                $response['error'] = false; 
                $response['message'] = "Update Successfully"; 
            }else{
                $response['error'] = true; 
                $response['message'] = "Please try again, Error occured";  
            }
        }else{
            $response['error'] = true;
            $response['message'] = "Plese upload necessary File";
        }
       

    }

}else{
    $response['error'] = true; 
    $response['message'] = "Invalid Request Method";
}
echo json_encode($response);