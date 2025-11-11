# problems

:db/id id
:num int
:avail "yes"
:problem text
:datetime java-time

# current problem/redis? use max?

:db/id id
:p/num int

# answers

:db/id id
:login string
:answer text
:p/num int
:datetime java-time

# scores

:db/id id
:login string
:p/num int
:datetime java-time
