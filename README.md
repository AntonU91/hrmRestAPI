# hrmRestAPI
For testing my project you might use *test.sql*  file which is located in * *src/main/resources* *.
## References:
### For manipulating with project:

| HTTP Method | Endpoint | Description |
| ------------- | ------------- |-------- |
| GET      | /projects                                      | get all project                                                        |
| POST     | /projects                                      | create new project
| GET      | /projects/{id}                                 | get project by id |
| PUT      | /projects/{projectId}/employees/{employeeId}   | assign employee with specified id on project with specified id |
| DELETE   | /projects/{projectId}/employees/{employeeId}   | delete employee with specified id from project with specified id |
| PUT      | /projecs/{projectId}/manager/{managerId}       | assign project manager with specified id on project with specified id |
| DELETE   | /projecs/{projectId}/manager/{managerId}       | delete project manager with specified id from project with specified id |
| DELETE   | /projects/{id}                                 | delete project with specified id |

### For manipulating with employee:

| HTTP Method | Endpoint | Description |
| ------------- | ------------- |-------- |
| GET      | /employees                                     | get all employees                                                     |
| POST     | /employees                                     | create new employee
| GET      | /employees/{id}                                 | get employee by id |
| PUT      | /employees/{id}/position                       | update position employee with specified id |
| PUT      | /employees/{id}/experience                     | update  experience employee with specified id |
| DELETE   | /employees/{id}                                 | delete employee with specified id |

### For manipulating with project manager:
| HTTP Method | Endpoint | Description |
| ------------- | ------------- |-------- |
| GET      | /managers                                     | get all project managers                                                   |
| POST     | /managers                                      | create new project manager
| PUT      | /managers/{id}/surname                         | udate surname of project manager with specified id  |
| GET       | /managers/{id}                                |  get  project manager  by id |
| DELETE   | /managers/{id}                                 | delete  project manage with specified id |
