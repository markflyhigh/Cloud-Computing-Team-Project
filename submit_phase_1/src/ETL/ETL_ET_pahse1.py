#!/usr/bin/python

import string
import sys
import os
import json


def main(argv):
	month={u'Jan':u'01',u'Feb':u'02',u'Mar':u'03',u'Apr':u'04',u'May':u'05',u'Jun':u'06',u'Jul':u'07',u'Aug':u'08',u'Sep':u'09',u'Oct':u'10',u'Nov':u'11',u'Dec':u'12'}
	# inFile = open("part-00000-30.txt","r")
	# lines = inFile.readlines()
	line = sys.stdin.readline()
	try:
		while line:
			tmp=json.loads(line)
			time=tmp["created_at"].split(" ",5)
			time=time[5]+'-'+month[time[1]]+'-'+time[2]+'+'+time[3]
			output=time+u'\t'+str(tmp["user"]["id"])+u'\t'+str(tmp["id"])
			print output
			line =  sys.stdin.readline()
	except "end of file":
		return None

	# for line in lines:
	# 	tmp=json.loads(line)
	# 	time=tmp["created_at"].split(" ",5)
	# 	time=time[5]+'-'+month[time[1]]+'-'+time[2]+'+'+time[3]
	# 	output=time+u'\t'+str(tmp["user"]["id"])+u'\t'+str(tmp["id"])
	# 	print output

	return

if __name__ == "__main__":
	main(sys.argv)
