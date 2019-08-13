# EasyLearningApp
Web application. Online learning portal.

This is a training project, so it has limited functionality.


Roles: GUEST, USER, AUTHOR, ADMIN

Options:
 
  USER: view video-lessons, deposit to acc balance via card, buy course by card or from balance,  view payment history,
        view purchased courses list, rate authors of purchased courses, rate purchased courses
        
  AUTHOR:  cash-out from balance to card, create courses and send to review, edit course, edit course image, 
          receive funds from user's purchases, view payment history, view own courses and their status
          
  ADMIN: approve or decline courses and their images, approve or decline account avatars, delete comments to author and course marks
  
  EVERYONE: change language, search courses, change account info, change account avatar, 
           change password, view courses and author's information,
           restore password (receive link on email)





SOME IMPLEMENTATION FEATURES

• Pagination: search course, view account payments
• On pages active options depends on role (custom tags implementation). It includes "learning page", "buy course", "rate course", "edit course" etc.
• File storage is outside of the project folder. Path specified in a property file.
• When a user sends an avatar or course image for verification, it is loaded into a separate directory with the name corresponding to the user id. After approval by the administrator, the old image is deleted and the new image is moved to the directory of active user images. 
• Personal information editable (not all, but mostly)
• Change avatar command (for a course or user) sends an image on review. Before admin approval, active in the previous image. When an admin approves the new image, it replaces the previous image. Approval can be declined, the image will not change as a result.
• Course state: frozen, on review approved. Only approved courses can be purchased or showed from search function. The state is shown on the author’s "My courses" page.
• Create/edit course: new/updated courses receive "ON REVIEW" status. They will not be shown in search results. After the admin's approval, they become visible. The author cannot delete or change the content of previously placed chapters and lessons in a particular course, because of the situation when someone has purchased a course (and he expected to see exactly what he bought). But it's still possible to add new chapters and lessons (placed chapters included).
• Payments. There are 5 types of payments: buy course by card (user), buy a course from the balance (user), deposit to balance by card (user), cash-out to a card from the balance (author), receive funds automatically sale on account balance after a course (author). Payments that are using card are conditional, all they need is a card number (not real of course), it's just a simulation. At present time full amount of money paid for course goes to the author, of course, but in the future it's possible to set percent of funds, that will go to the owner of application ))
• Registered users can once rate purchased courses and author, who sold at least one course to a particular user. Mark includes value (1-5) and comment. Comment can be deleted by admin (value does not change, however).


***** To be continued  *****