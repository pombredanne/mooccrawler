#!/bin/sh
now="$(date +'%d-%H-%M')"
mkdir /home/rene/mooccrawl/$now
cd /home/rene/mooccrawl/$now
for  i in 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26
do
wget "https://moocfellowship.org/submissions?order=vote_count+desc&page=$i" --output-document /home/rene/mooccrawl/$now/page$i.htm --output-file /home/rene/mooccrawl/$now/transaction.log
done;

