import re
import json
import math
import numpy as np
import collections
import xml.dom.minidom
from stemming.porter2 import stem

# open trec.sample.xml
dom = xml.dom.minidom.parse('trec.5000.xml')
rootdata = dom.documentElement

# store number and text
# DOCNO[0]:  1
# Text[0]:   He likes to wink, he likes to drink
docno_list = rootdata.getElementsByTagName('DOCNO')
headline_list = rootdata.getElementsByTagName('HEADLINE')
text_list = rootdata.getElementsByTagName('TEXT')

# create dictionary: num_text_dict
num_text_dict = {}
for i in range(len(text_list)):
    num_text_dict[int(docno_list[i].firstChild.data)] = str(headline_list[i].firstChild.data).strip() + " " + str(text_list[i].firstChild.data).strip()

# store word position 
word_position_dict = {}  # store word position in every document

with open('englishST.txt', 'r') as eng:
    eng_str = eng.read()
eng_list = eng_str.split()  # stopping words list

r = """[0-9!#$%&'"()*+,-./:;<=>?@[\]^_`{|}~\n]"""
for key, value in num_text_dict.items():
    # delete punctuation
    no_punct = ""
    no_punct = re.sub(r, ' ', value)
    no_punct = no_punct.lower()
    no_list = no_punct.split()  # text words list
    
    # Stopping 
    stop_list = []
    for j in no_list:
        if (j not in eng_list):
            stop_list.extend([j])
    
    # Normalisation and Stemming
    norm_list = []
    for m in stop_list:
        norm_list.append(stem(m))    
        
    # generate position list in every document
    doc_position_list = []
    for i in range(len(norm_list)):
        doc_position_list.extend([[i+1, norm_list[i]]])
    
    word_position_dict[key] = doc_position_list

book_number_list = list(word_position_dict.keys())
print("N=" + str(len(book_number_list)))

# special words
specialwords_list = []
for value in word_position_dict.values():
    for i in range(len(value)):
        if (value[i][1] not in specialwords_list):
            specialwords_list.extend([value[i][1]])          
specialwords_list.sort()

# Through the dictionary to get "Term" and its "Position"
# {'Term1': [[doc1, pos1], [doc2, pos2], [doc3, pos3]
#  'Term2': [[doc1, pos1], [doc2, pos2], [doc3, pos3]}
term_position_dict = {}

for i in range(len(specialwords_list)):
    term_position_dict[specialwords_list[i]] = []

for key, value in word_position_dict.items():
    for i in range(len(value)):
        term_position_dict[value[i][1]].extend([[key, value[i][0]]])

# clear
file = open('index_ts_pos.txt', 'w').close()
# wirte
js = json.dumps(term_position_dict) 
file = open('index_ts_pos.txt', 'w') 
file.write(js) 
file.close() 

# Through the dictionary to get "Term" and its "Position" in every document
# {'Term1': {doc1: [position1, position2], doc2: [position1, position2]}
#  'Term2': {doc1: [position1, position2, position3], doc2: [position1, position2]}}
term_doc_pos_dict = {}

for key, value in term_position_dict.items():
    each_doc_pos_dict = {}
    doc_pos_list = [value[0][1]]  # [8]
    each_doc_pos_dict[value[0][0]] = [value[0][1]]  # doc1: [position1]   1: [8]
    
    for i in range(len(value)-1):
        if (value[i+1][0] == value[i][0]):
            doc_pos_list.extend([value[i+1][1]])  # if doc number don't change, extend doc_pos_list
        else:  # else generate doc->positonlist
            each_doc_pos_dict[value[i][0]] = doc_pos_list  # generate {dco1: [pos1, pos2, pos3]}
            doc_pos_list = [value[i+1][1]]
    
    each_doc_pos_dict[value[len(value)-1][0]] = doc_pos_list  # the last doc_pos_list
    doc_pos_list = []
    
    term_doc_pos_dict[key] = each_doc_pos_dict

# clear
file = open('index_ts.txt', 'w').close()
# write
js = json.dumps(term_doc_pos_dict) 
file = open('index_ts.txt', 'w') 
file.write(js) 
file.close() 



##############################
##### generate index.txt #####
##############################
# Term -> Position
file = open('index_ts_pos.txt', 'r') 
js = file.read()
term_position_dict = json.loads(js) 
file.close() 
doc = open('index.txt', 'w')

# input from term_position_dict
# {'Term1': [[doc1, pos1], [doc2, pos2], [doc3, pos3]
#  'Term2': [[doc1, pos1], [doc2, pos2], [doc3, pos3]}
for key, value in term_position_dict.items():
    print(key + ":" + str(len(term_doc_pos_dict[key])), file=doc)
    print("\t" + str(value[0][0]) + ": " + str(value[0][1]), end="", file=doc)  # do not wrap
    for i in range(len(value)-1):
        if (value[i+1][0] == value[i][0]):
            print(", " + str(value[i+1][1]), end="", file=doc)
        else:
            print("", file=doc)
            print("\t" + str(value[i+1][0]) + ": " + str(value[i+1][1]), end="", file=doc)
            
    print("", file=doc)
doc.close()


######################################################################################################


# Search Module
# search one word
def word_search(w_s, search_dict):
    w_s = stem(w_s.lower())
    key_list = search_dict[w_s].keys()
    key_list = [int(x) for x in list(key_list)]
    return key_list

def link_search(s, search_dict):
    r = """["]"""
    s = re.sub(r, '', s)
    s_list = s.split()
    w1 = s_list[0]
    w1 = stem(w1.lower()) 
    w2 = s_list[1]
    w2 = stem(w2.lower()) 
    w1_pos_list = list(search_dict[w1])
    w2_pos_list = list(search_dict[w2])
    
    link_list = []
    
    if ((len(w1_pos_list) == 0) or (len(w2_pos_list) == 0)):
        return link_list
    else:
        w1_len = len(w1_pos_list)
        w2_len = len(w2_pos_list)
        i = 0
        j = 0
        #  flag = ((i < (w1_len-2)) and (j < (w2_len-2)))  # end condition
        while ((i < (w1_len-1)) and (j < (w2_len-1))):  # double index
            if (w1_pos_list[i][0] == w2_pos_list[j][0]):  # two words are in the same document
                if ((w2_pos_list[j][1]-w1_pos_list[i][1]) == 1):
                    link_list.extend([w1_pos_list[i][0]])
                    i = i + 1
                    j = j + 1
                elif ((w2_pos_list[j][1] - w1_pos_list[i][1]) > 1):
                    i = i + 1
                elif ((w2_pos_list[j][1] - w1_pos_list[i][1]) < 1):
                    j = j + 1   
            elif (w1_pos_list[i][0] > w2_pos_list[j][0]):
                j = j + 1
            elif (w1_pos_list[i][0] < w2_pos_list[j][0]):
                i = i + 1
    
    link_list = list(set(link_list))
    link_list.sort()
    
    return link_list

# search not word or link words
def not_search(a, search_dict, term_position_dict):
    r = """["]"""
    a_list = a.split("NOT")
    a_list = [x.strip() for x in a_list]
    a = a_list[1]
    if ('"' in a):  # it is a link_word
        # use link_search function
        a = re.sub(r, ' ', a)  # delete "
        a_not_list = link_search(a, term_position_dict)  
    else:  # single word
        # use word_search function
        a_not_list = word_search(a, search_dict) 
        
    a_key_list = book_number_list.copy()
    for i in a_not_list:
        if (i in a_key_list):
            a_key_list.remove(i)  # get not-a list
    
    return a_key_list

def distance_search(s, search_dict):
    r = """[!#$%&'"()*+,-./:;<=>?@[\]^_`{|}~\n]"""
    s_punct = re.sub(r, ' ', s)
    s_list = s_punct.split()  # text words list
    distance = int(s_list[0])
    w1 = s_list[1]
    w1 = stem(w1.lower()) 
    w2 = s_list[2]
    w2 = stem(w2.lower()) 
    w1_pos_list = list(search_dict[w1])
    w2_pos_list = list(search_dict[w2])

    distance_list = []
    
    if ((len(w1_pos_list) == 0) or (len(w2_pos_list) == 0)):
        return link_list
    else:
        w1_len = len(w1_pos_list)
        w2_len = len(w2_pos_list)
        i = 0
        j = 0
        while ((i < (w1_len-1)) and (j < (w2_len-1))):  # double index
            if (w1_pos_list[i][0] == w2_pos_list[j][0]):  # two words are in the same document
                if (abs(w2_pos_list[j][1]-w1_pos_list[i][1]) <= distance):  # distance: distance between two words
                    distance_list.extend([w1_pos_list[i][0]])
                    i = i + 1
                    j = j + 1
                elif ((w2_pos_list[j][1] - w1_pos_list[i][1]) > distance):
                    i = i + 1
                elif ((w2_pos_list[j][1] - w1_pos_list[i][1]) < (-distance)):
                    j = j + 1   
            elif (w1_pos_list[i][0] > w2_pos_list[j][0]):
                j = j + 1
            elif (w1_pos_list[i][0] < w2_pos_list[j][0]):
                i = i + 1
    
    distance_list = list(set(distance_list))
    distance_list.sort()
    
    return distance_list

def or_search(s, search_dict, term_position_dict):
    w_list = s.split(' ')
    for i in range(len(w_list)):
        if (w_list[i] == 'OR'):
            or_pos = i
    
    # two words
    a = ' '.join(w_list[:or_pos])
    b = ' '.join(w_list[or_pos+1:])
    r = """["]"""
    
    # deal with NOT
    if ("NOT" in a):
        a_list = a.split("NOT")
        a_list = [x.strip() for x in a_list]
        a = a_list[1]
        if ('"' in a):  # it is a link_word
            # use link_search function
            a = re.sub(r, ' ', a)  # delete "
            a_not_list = link_search(a, term_position_dict)   
        else:  # single word
            # use word_search function
            a_not_list = word_search(a, search_dict)    
        a_key_list = book_number_list.copy()
        for i in a_not_list:
            if (i in a_key_list):
                a_key_list.remove(i)  # get not-a list
    else:
        if ('"' in a):  # it is a link_word
            # use link_search function
            a = re.sub(r, ' ', a)  # delete "
            a_key_list = link_search(a, term_position_dict)   
        else:  # single word
            # use word_search function
            a_key_list = word_search(a, search_dict)
    
    if ("NOT" in b):
        b_list = b.split("NOT")
        b_list = [x.strip() for x in b_list]
        b = b_list[1]
        if ('"' in b):  # it is a link_word
            # use link_search function
            b = re.sub(r, ' ', b)  # delete "
            b_not_list = link_search(b, term_position_dict)   
        else:  # single word
            # use word_search function
            b_not_list = word_search(b, search_dict)
        b_key_list = book_number_list.copy()
        for i in b_not_list:
            if (i in b_key_list):
                b_key_list.remove(i)  # get not-b list 
    else:
        if ('"' in b):  # it is a link_word
            # use link_search function
            b = re.sub(r, ' ', b)  # delete "
            b_key_list = link_search(b, term_position_dict)   
        else:  # it is a single word
            # use word_search function
            b_key_list = word_search(b, search_dict)   
    
    # get OR list
    or_list = a_key_list
    for i in b_key_list:
        if (i not in or_list):
            or_list.extend([i])
    or_list.sort()
    
    return or_list

def and_search(s, search_dict, term_position_dict):
    w_list = s.split(' ')
    for i in range(len(w_list)):
        if (w_list[i] == 'AND'):
            and_pos = i
    
    # two words
    a = ' '.join(w_list[:and_pos])
    b = ' '.join(w_list[and_pos+1:])
    r = """["]"""
   
    # deal with NOT
    if ("NOT" in a):
        a_list = a.split("NOT")
        a_list = [x.strip() for x in a_list]
        a = a_list[1]
        if ('"' in a):  # it is a link_word
            # use link_search function
            a = re.sub(r, ' ', a)  # delete "
            a_not_list = link_search(a, term_position_dict)   
        else:  # single word
            # use word_search function
            a_not_list = word_search(a, search_dict)    
        a_key_list = book_number_list.copy()
        for i in a_not_list:
            if (i in a_key_list):
                a_key_list.remove(i)  # get not-a list
    else:
        if ('"' in a):  # it is a link_word
            # use link_search function
            a = re.sub(r, ' ', a)  # delete "
            a_key_list = link_search(a, term_position_dict)   
        else:  # single word
            # use word_search function
            a_key_list = word_search(a, search_dict)   
    
    if ("NOT" in b):
        b_list = b.split("NOT")
        b_list = [x.strip() for x in b_list]
        b = b_list[1]
        if ('"' in b):  # it is a link_word
            # use link_search function
            b = re.sub(r, ' ', b)  # delete "
            b_not_list = link_search(b, term_position_dict)   
        else:  # single word
            # use word_search function
            b_not_list = word_search(b, search_dict) 
        b_key_list = book_number_list.copy()
        for i in b_not_list:
            if (i in b_key_list):
                b_key_list.remove(i)  # get not-b list 
    else:
        if ('"' in b):  # it is a link_word
            # use link_search function
            b = re.sub(r, ' ', b)  # delete "
            b_key_list = link_search(b, term_position_dict)   
        else:  # it is a single word
            # use word_search function
            b_key_list = word_search(b, search_dict)   
      
    # get AND list
    and_list = []
    for i in a_key_list:
        if (i in b_key_list):
            and_list.extend([i])
    and_list.sort()
    
    return and_list

# my search module
def search(s, term_doc_pos_dict, term_position_dict):
    doc_list = []
    if ("AND" in s):
        doc_list = and_search(s, term_doc_pos_dict, term_position_dict)
    elif ("OR" in s):
        doc_list = or_search(s, term_doc_pos_dict, term_position_dict)
    elif ("#" in s):
        doc_list = distance_search(s, term_position_dict)
    elif ("NOT" in s):
        doc_list = not_search(s, term_doc_pos_dict, term_position_dict)
    elif (len(s.split()) > 1):
        doc_list = link_search(s, term_position_dict)
    else:
        doc_list = word_search(s, term_doc_pos_dict)
        
    return doc_list


##################################
## generate results.boolean.txt ##
##################################
# Input Query txt
with open('queries.boolean.txt', 'r') as f:
    line_list = f.readlines()
f.close()

query_dict = {}
num_list = []
l_list = []

# get the number
for i in range(len(line_list)):  
    num_list.extend([line_list[i].split()])  # split 
# get the line
for j in range(len(line_list)):
    l_list.extend([' '.join(num_list[j][1:])])
for k in range(len(l_list)):
    query_dict[num_list[k][0]] = l_list[k]

# get results.boolean dict
# boolean_dict = {int1: list[int], int2: list[int]}
boolean_dict = {}
for i in range(len(query_dict)):
    boolean_dict[i+1] = search(query_dict[str(i+1)], term_doc_pos_dict, term_position_dict)
    print(len(boolean_dict[i+1]))

doc = open('results.boolean.txt', 'w')
for key, value in boolean_dict.items():
    for i in range(len(value)):
        print(str(key) + "," + str(value[i]), file=doc)
doc.close()


#####################################################################################################


# Term -> (Document -> Position)
# input term_doc_pos_dict
# {'Term1': {doc1: [position1, position2], doc2: [position1, position2]}
#  'Term2': {doc1: [position1, position2, position3], doc2: [position1, position2]}}
file = open('index_ts.txt', 'r') 
js = file.read()
term_doc_pos_dict = json.loads(js) 
file.close() 
# Term -> Position
# input from term_position_dict
# {'Term1': [[doc1, pos1], [doc2, pos2], [doc3, pos3]
#  'Term2': [[doc1, pos1], [doc2, pos2], [doc3, pos3]}
file = open('index_ts_pos.txt', 'r') 
js = file.read()
term_position_dict = json.loads(js) 
file.close() 

# stopping words list
with open('englishST.txt', 'r') as eng:
    eng_str = eng.read()
eng_list = eng_str.split()  

def takeSecond(elem):
    return elem[1]

def getScore(s, term_doc_pos_dict):
    s_list = s.split()
    for m in range(len(s_list)):
        s_list[m] = s_list[m].lower()  # lower()

    # Stopping 
    stop_list = []
    for i in s_list:
        if (i not in eng_list):
            stop_list.extend([i])
    
    # Normalisation
    w_list = []
    for j in stop_list:
        w_list.extend([stem(j)])
    
    print(w_list)
    
    # get document list
    doc_list = list(term_doc_pos_dict[w_list[0]].keys()) 
    for i in range(len(w_list)-1):
        doc_list = list(set(doc_list).union(term_doc_pos_dict[w_list[i+1]].keys())) 
    for j in range(len(doc_list)):
        doc_list[j] = int(doc_list[j])
    doc_list.sort()
    
    # get score list
    score_list = []
    for i in range(len(doc_list)):
        score_doc = 0
        for j in range(len(w_list)):           
            if (str(doc_list[i]) in list(term_doc_pos_dict[w_list[j]].keys())):
                a = 1 + math.log10(len(term_doc_pos_dict[w_list[j]][str(doc_list[i])]))
                # print(a)
                b = math.log10(5000/len(term_doc_pos_dict[w_list[j]].keys()))
                # print(b)
                score_doc = score_doc + a * b
        score_list.extend([[int(doc_list[i]), format(score_doc, '.4f')]])  
    
    # sort according to 1st column
    score_list.sort(key=takeSecond, reverse=True)
              
    return score_list

# Input query txt
with open('queries.ranked.txt', 'r') as f:
    line_list = f.readlines()
f.close()

term_score_dict = {}
num_list = []
l_list = []
r = """[0-9?]"""

# get the number
for i in range(len(line_list)):  
    num_list.extend([line_list[i].split()])  # split 
# get the line
for j in range(len(line_list)):
    l_list.extend([re.sub(r, '', line_list[j]).strip()])  # detele 0-9

for k in range(len(l_list)):
    term_score_dict[int(num_list[k][0])] = getScore(l_list[k], term_doc_pos_dict)
    


#################################
## generate results.ranked.txt ##
#################################
doc = open('results.ranked.txt', 'w')
for key, value in term_score_dict.items():
    if (len(value) > 150):
        for i in range(150):
            print(str(key) + "," + str(value[i][0]) + "," + str(value[i][1]), file=doc)
    else:
        for i in range(len(value)):
            print(str(key) + "," + str(value[i][0]) + "," + str(value[i][1]), file=doc)
doc.close()

