<?php
require_once '../includes/DbConnection.php';

class CheckRegistrationData {
    private $con; 

    public function validate($name, $email, ) {
        $validationErrors = array();

        // Validate name
        if (empty($name)) {
            $validationErrors[] = "Name is required";
        } else if (strlen($name) < 3) {
            $validationErrors[] = "Name should be minimum of 5 characters long";
        } else if (!preg_match("/^[A-Za-z]+(?: [A-Za-z]+)*$/", $name)) {
            $validationErrors[] = "Name should 3cahratcer long\nonly contain alphaptes and underscore characters";
        }
        
        // Validate email
        if (empty($email)) {
            $validationErrors = "Email is required";
        } else if (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
            $validationErrors = "Invalid email format";
        }
        //checkexisting email
        $db = new DbConnect();
        $this->con = $db->connect();
        $stmt = $this->con->prepare("SELECT email FROM registration WHERE  email = ?");
			$stmt->bind_param("s", $email);
			$stmt->execute(); 
			$stmt->store_result();
            if( $stmt->num_rows > 0){
                $validationErrors= "Email already Registered";

            }       


     

        return $validationErrors;
    }
}
