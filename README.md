I'm Parameswara Rao Mallela


For testing Please goto:http://localhost:4567/
quick test login details
param@gmail.com
Qwerty123
About Poll Project...
Bootstrap and javascript is used.


Pages:
1.Home
2.Login
3.Registration
4.Polls
5.Poll

1.Home
->Allows user to Login and Register

2.Login
->User can Login here or go back to registration page.
->Validating Email (format) and password cannot be empty
->Submit is disabled when the E-Mail or password is empty
->Text box is changed to Red if E-mail format is wrong or password is empty
->Remember login details 

3.Registration
->Validating all fields (cannot be empty)
->Can go back to login page
->Password and VPassword validation
->E-Mail has to be in proper format.

4.Polls
->If this pages is accessed directly no content will be displayed
->if logged in Welcom by NAME or ask the user to login or register
->options are given to login or register
->If the user is logged in. Welcome with email
->Displays list of sample polls and can be removed..
->Can add polls
->Select any poll to see further details

5.Poll
->poll details are displayed in this page
->Can vote or remove or add choice
->Poll ball chart and pie chart are displayed below the poll details

SMS Voting..
->sms voting in working fine. Vote has to be made using pollid and choiceid
->twilio and ngrock has to be configured

HTML:
freg-If registration failed
home-Home page
login-Login Page
Loginchekc-Login is failed
poll-details about selected poll
polls-list of polls
registration-for signup
sreg-If registration is sucessfull asks to login again

