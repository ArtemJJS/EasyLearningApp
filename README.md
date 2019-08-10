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


Features


* Pagination: search course, view account payments
* On pages active options depends on role (custom tags realisation). It icludes "learning page", "buy course", "rate course", "edit course" etc.
* File storage is outside of project folder. Path specified in property file.
* Personal information editable (not all, but mostly)
* Change avatar command (for course or user) send image on review. Before admin aprovement, active is previous image. When admin approves 
  new image, it replace previous image. Approvement can be declined, image will not chage as a result.
* Course state: frozen, on review, approved. Only approved courses can be purchased or showed from search function. It is shown 
  on author "My courses" page.
* Create/edit course: new\updated courses receive "ON REVIEW" status. They will not be shown in search results. After admin's approvement
  they become visible. Author has no possibility to delete or change content of previously placed chapters and lessons in particular course,
  because of the situation when someone has purchased course (and he expected to see exactly what he bought). But it's still possible to add new chapters
  and lessons (placed chapters included).
* Payments. There are 5 types of payments: buy course by card (user), buy course from balanse (user), deposit to balance by card (user), 
  cash-out to card from balance (author), automatically receive funds after course sale on acc balance (author). Payments that are using card are conditionally, 
  all they need is a card number (not real of course), it's just a simulation. At present time full amount of money payed for course goes to author of
  course, but in the future it's possible to set percent of funds, that will go to owner of application ))
* Registered users can once rate purchased courses and author, who solr at least one course to particular user. Mark includes value (1-5) 
  and comment. Comment can be deleted by admin (value not change however).

***** To be contibued  *****