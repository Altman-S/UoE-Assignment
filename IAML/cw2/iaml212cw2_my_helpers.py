
##########################################################
#  Python module template for helper functions of your own (IAML Level 11)
#  Note that:
#  - Those helper functions of your own for Questions 1 and 2 should be defined in this file.
#  - You can decide function names by yourself.
#  - You do not need to include this header in your submission.
##########################################################

# sum of all numbers in a list
# used in Q1.4
def sumOfList(l):
    sum = 0
    for i in range(len(l)):
        sum += l[i]

    return sum

# used in Q1.4 Q2.6
def takeSecond(elem):
    return elem[1]

# used in Q1.5
def productSum(list1, list2):
    list = []
    sum = 0.0
    for i in range(len(list1)):
        list.extend([list1[i] * list2[i]])

    for j in range(len(list)):
        sum = sum + list[j]

    return sum

