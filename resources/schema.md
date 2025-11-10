# problems

:db/id id
:num int
:avail "yes"
:problem text
:datetime java-time

# current problem

:db/id id
:current problem/num

# answers

:db/id id
:login string
:answer text
:p/num init
:datetime java-time

# scores

:db/id id
:login string
:p/num init
:datetime java-time
