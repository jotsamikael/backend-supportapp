
************************LOGIN************************


POST /api/v1/user/login HTTP/1.1
Host: localhost:8095
Content-Type: application/json
Content-Length: 64

{

    "userName":"doe91",
    "password":"VWXUAknZAP"
}


************************REGISTER************************
POST /api/v1/user/register HTTP/1.1
Host: localhost:8095
Content-Type: application/json
Content-Length: 108

{
"firstName":"John",
"lastName":"Doe",
"userName":"doe91",
"email":"doe91@gmail.com"
}