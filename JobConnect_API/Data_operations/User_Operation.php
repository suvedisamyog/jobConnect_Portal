<?php

class User{

    private $con;

    function __construct()
    {
        require_once '../includes/DbConnection.php';
        $db = new DbConnect();
        $this->con = $db->connect();        
    }

    public function userProfile($uName,$uEmai,$uDob,$uPhone,$uEducation,$uBio,$uImg,$uCv,$uCategories,$uIndustry){

        if($this->isUserExist($uEmai)){
            return 0; 

        }else {
            $stmt=$this->con->prepare("INSERT INTO user_profile (uName,uEmail,uDob,uPhone,uEducation,uBio,uImg,uCv,uCategories,uIndustry) VALUES (?,?,?,?,?,?,?,?,?,?)");
            $stmt->bind_param("ssssssssss",$uName,$uEmai,$uDob,$uPhone,$uEducation,$uBio,$uImg,$uCv,$uCategories,$uIndustry);
            if ($stmt->execute()) {
              return 1;
          } else {  
              return 2;
          }
          
        }  
        
      }
      
      public function update_reg_complete($uEmai){
        $value="true";
        $stmt = $this->con->prepare("UPDATE registration SET isComplete = ? WHERE Email  = ?");
        $stmt->bind_param("ss",$value, $uEmai);
        if ($stmt->execute()) {
          return 1;
      } else {
          return 2;
      }
      
      }

      private function isUserExist( $uEmai){
        $stmt = $this->con->prepare("SELECT uEmail FROM user_profile WHERE  uEmail = ?");
        $stmt->bind_param("s",  $uEmai);
        $stmt->execute(); 
        $stmt->store_result(); 
        return $stmt->num_rows > 0; 
    }  
    
    public function fetchUserData($email){
        $stmt=$this->con->prepare("SELECT * FROM user_profile WHERE uEmail=? ");
        $stmt->bind_param("s",  $email);
        $stmt->execute(); 
        $result = $stmt->get_result();
        $datas = array();
        while($row=$result->fetch_assoc()){
            $datas[] = $row;
        }
        return $datas;

    }

    public function updateUser($uName, $uEmail, $uDob, $uPhone, $uEducation, $uBio, $pathImg, $uIndustry, $uCategories){
        $stmt = $this->con->prepare("UPDATE user_profile SET uName = ?, uDob = ?, uPhone = ?, uEducation = ?, uBio = ?, uImg = ?, uCategories = ?, uIndustry = ? WHERE uEmail = ?");
     
        $stmt->bind_param("sssssssss", $uName, $uDob, $uPhone, $uEducation, $uBio, $pathImg, $uCategories, $uIndustry, $uEmail);
        
        if($stmt->execute()){
            return true;
        }else{
            return false;
        }   
    }
    
    public function generateCv($skill, $education, $experience, $per_address, $temp_address, $email, $gender) {
        // Check if the email already exists
        $stmt = $this->con->prepare("SELECT email FROM cv WHERE email = ?");
        $stmt->bind_param("s", $email);
        $stmt->execute();
        $stmt->store_result();
        
        if ($stmt->num_rows > 0) {
            // Email already exists, delete the existing data
            $deleteStmt = $this->con->prepare("DELETE FROM cv WHERE email = ?");
            $deleteStmt->bind_param("s", $email);
            $deleteStmt->execute();
            $deleteStmt->close();
        }
        
        // Insert the new data
        $insertStmt = $this->con->prepare("INSERT INTO cv (email, gender, temp_address, per_address, experience, education, skill) VALUES (?, ?, ?, ?, ?, ?, ?)");
        $insertStmt->bind_param("sssssss", $email, $gender, $temp_address, $per_address, $experience, $education, $skill);
        
        if ($insertStmt->execute()) {
            $insertStmt->close();
            return true;
        } else {
            $insertStmt->close();
            return false;
        }
    }
 
    
    public function getCv($email){
        $stmt = $this->con->prepare("SELECT cv.*, user_profile.*    FROM cv    JOIN user_profile ON cv.email = user_profile.uEmail   WHERE cv.email = ?");    
        
        $stmt->bind_param("s", $email);
        $stmt->execute();
        $result = $stmt->get_result();
        $data = array();
        while ($row = $result->fetch_assoc()) {
            $data[] = $row;
        }
    
        return $data;
    

    }

    public function checkCv($email) {
        $stmt = $this->con->prepare("SELECT email FROM cv WHERE email = ?");
        $stmt->bind_param("s", $email);
        $stmt->execute();
        $result = $stmt->get_result();
        $row = $result->fetch_assoc();
        return !empty($row);
    }
 
    
    

}