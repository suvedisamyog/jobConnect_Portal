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
        $uPhone = isset($_POST['uPhone']) ? $_POST['uPhone'] : '';
        $uIndustry = isset($_POST['uIndustry']) ? $_POST['uIndustry'] : '';     
        $uCategories = isset($_POST['uCategories']) ? $_POST['uCategories'] : '';
    

        if (!empty($uName) && !empty($uEmail) && !empty($uDob) && !empty($uEducation) && !empty($uImg) && !empty($uCategories) && !empty($uBio) && !empty($uIndustry) && !empty($uPhone)) {
            $pathImg='../Images/'. date("d-m-y") . '-' . time() . '-' . rand(10000,1000000) . '.jpeg';
        

            if((file_put_contents($pathImg,base64_decode($uImg)))){
                $db = new User();
                $userProfile = $db->updateUser($uName, $uEmail, $uDob,$uPhone, $uEducation,$uBio, $pathImg,$uIndustry, $uCategories);
        
                if ($userProfile) {
                    $response['error'] = false;
                    $response['message'] = "Profile Update Successfully";        
                } 
                else {
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