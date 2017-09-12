<?php

/**
 * @author Hyunho Jeon
 * referencing from the register.php from the following link
 * @link http://www.androidhive.info/2012/01/android-login-and-registration-with-php-mysql-and-sqlite/ Complete tutorial
 */

require_once 'include/DB_Functions.php';
$db = new DB_Functions();

// json response array
$response = array("error" => FALSE);

if (isset($_POST['user_id'])) {

    // receiving the post params
    $user_id = $_POST['user_id'];
 
    // check if user is already existed with the same email
    
    /*if ($db->isUserExisted($email)) {
        // user already existed
        $response["error"] = TRUE;
        $response["error_msg"] = "User already existed with " . $email;
        echo json_encode($response);
    }*/ 



    // storing the attendance
    $user = $db->storeAttendance($user_id);
    if ($user) {
        // user stored successfully
        $response["error"] = FALSE;
        $response["user"]["user_id"] = $user["user_id"];
        $response["user"]["attended_at"] = $user["attended_at"];
        echo json_encode($response);
    } else {
        // user failed to store
        $response["error"] = TRUE;
        $response["error_msg"] = "Unknown error occurred in registration!";
        echo json_encode($response);
    }
    
} 
else {
    $response["error"] = TRUE;
    $response["error_msg"] = "there is an error.. errr !";
    echo json_encode($response);
}
?>

