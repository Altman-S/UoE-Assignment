#!/usr/bin/env python
# coding: utf-8

# # Import Package

import sklearn
import collections

# regular expressions
import re
# for string.punctuation: list of punctuation characters
import string
from stemming.porter2 import stem
from collections import Counter

# import this for storing our BOW format
import scipy
from scipy import sparse
# scikit learn. Contains lots of ML models we can use
# import the library for support vector machines
from sklearn import svm
from sklearn import ensemble
from sklearn.metrics import classification_report
from sklearn.model_selection import train_test_split
from sklearn.feature_extraction.text import TfidfTransformer

import numpy as np
import csv

# from gensim.test.utils import common_texts
from gensim.models.ldamodel import LdaModel
from gensim.corpora.dictionary import Dictionary

##########################
# #                    # #
# # 1. IR Evaluation   # #
# #                    # #
##########################

# read qrels.csv and system_results.csv
qrels_mat = np.loadtxt(open("qrels.csv","rb"), delimiter=",", skiprows=1)
system_results_mat = np.loadtxt(open("system_results.csv","rb"), delimiter=",", skiprows=1)

system_number = 6
query_number = 10
system_number_index = np.array([x[0] for x in system_results_mat])
system_query_index = np.array([x[1] for x in system_results_mat])
query_id_index = np.array([x[0] for x in qrels_mat])
doc_id_index = np.array([x[1] for x in qrels_mat])

query_rel_doc = []
doc_rel_dict = {}
# store relevant doc_id for every query
for i in range(query_number):
    doc_array = qrels_mat[query_id_index==i+1]
    doc_list = [x[1] for x in doc_array]
    query_rel_doc.extend([doc_list])
    
    rel_dict = {}
    for j in range(len(doc_array)):
        rel_dict[doc_array[j][1]] = doc_array[j][2]
    
    doc_rel_dict[i+1] = rel_dict

# doc_rel_dict

# get P@10 for each system and each query
num_traverse = 10
p10_list = []
# traverse 6 system
for i in range(system_number):
    # get the array of system for every system 0-6
    system_array = system_results_mat[system_number_index==i+1]
    query_index = np.array([x[1] for x in system_array])
    
    p10_query = []
    # traverse 10 query
    for j in range(query_number):
        # get the array of query for every query 0-10
        query_array = system_array[query_index==j+1]
        query_10 = query_array[:10]
        doc_query_10 = [x[2] for x in query_10]
       
        num_rel = 0
        for doc in doc_query_10:
            if doc in query_rel_doc[j]:
                num_rel += 1
                
        p10_query.extend([num_rel/num_traverse])
            
    p10_list.extend([p10_query])

# p10_list

# get R@50 for each system and each query
r50_list = []
# traverse 6 system
for i in range(system_number):
    # get the array of system for every system 0-6
    system_array = system_results_mat[system_number_index==i+1]
    query_index = np.array([x[1] for x in system_array])
    
    r50_query = []
    # traverse 10 query
    for j in range(query_number):
        # get the array of query for every query 0-10
        query_array = system_array[query_index==j+1]
        query_50 = query_array[:50]
        doc_query_50 = [x[2] for x in query_50]
       
        num_relevant = len(query_rel_doc[j])  # store the num of relevant documents in recall
        num_rel = 0
        for doc in doc_query_50:
            if doc in query_rel_doc[j]:
                num_rel += 1
                
        r50_query.extend([num_rel/num_relevant])
            
    r50_list.extend([r50_query])

# r50_list

# get r-precision for each system and each query
rp_list = []
# traverse 6 system
for i in range(system_number):
    # get the array of system for every system 0-6
    system_array = system_results_mat[system_number_index==i+1]
    query_index = np.array([x[1] for x in system_array])
    
    rp_query = []
    # traverse 10 query
    for j in range(query_number):
        # get the array of query for every query 0-10
        num_relevant = len(query_rel_doc[j])  # store the num of relevant documents in recall
    
        query_array = system_array[query_index==j+1]
        query_n = query_array[:num_relevant]
        doc_query_n = [x[2] for x in query_n]
       
        num_rel = 0
        for doc in doc_query_n:
            if doc in query_rel_doc[j]:
                num_rel += 1
                
        rp_query.extend([num_rel/num_relevant])
            
    rp_list.extend([rp_query])

# rp_list

# get AP for each system and each query
ap_list = []
# traverse 6 system
for i in range(system_number):
    # get the array of system for every system 0-6
    system_array = system_results_mat[system_number_index==i+1]
    query_index = np.array([x[1] for x in system_array])
    
    ap_query = []
    # traverse 10 query
    for j in range(query_number):
        # get the array of query for every query 0-10   
        query_array = system_array[query_index==j+1]
        doc_query_n = [x[2] for x in query_array]
       
        ap = 0
        rel = 0
        traverse = 0
        # traverse all documents for each query
        for k in range(len(doc_query_n)):
            traverse += 1
            if doc_query_n[k] in query_rel_doc[j]:
                rel += 1
                ap += rel / traverse
        
        ap /= len(query_rel_doc[j])
                          
        ap_query.extend([ap])
            
    ap_list.extend([ap_query])

# ap_list

# get nDCG@10 for each system and each query
ndcg10_list = []
n = 10
ig_lists = []

# generate iG list
for i in range(query_number):
    doc_array = qrels_mat[query_id_index==i+1]
    ig_list = [x[2] for x in doc_array]
    
    if len(ig_list) < n:
        for j in range(n-len(ig_list)):
            ig_list.extend([0])
    else:
        ig_list = ig_list[:n]
    
    ig_lists.extend([ig_list])
    
# traverse 6 system
for i in range(system_number):
    # get the array of system for every system 0-6
    system_array = system_results_mat[system_number_index==i+1]
    query_index = np.array([x[1] for x in system_array])
    
    ndcg10_query = []
    # traverse 10 query
    for j in range(query_number):
        # get the array of query for every query 0-10   
        query_array = system_array[query_index==j+1]
        query_n = query_array[:n]
        doc_query_n = [x[2] for x in query_n]
       
        g_list = []
        # get G list 
        for k in range(len(doc_query_n)):
            if doc_query_n[k] in query_rel_doc[j]:
                g_list.extend([doc_rel_dict[j+1][doc_query_n[k]]])
            else:
                g_list.extend([0])
        
        # calculate DCG
        dcg = g_list[0]
        for p in range(len(g_list)-1):
            a = g_list[p + 1] / np.log2(p + 2)
            dcg += a
            
        # calculate iDCG  
        ig_list = ig_lists[j]
        idcg = ig_list[0]
        for q in range(len(ig_list)-1):
            b = ig_list[q + 1] / np.log2(q + 2)
            idcg += b
        
        if idcg == 0 :                    
            ndcg10_query.extend([0])
        else:
            ndcg10_query.extend([dcg / idcg])
            
    ndcg10_list.extend([ndcg10_query])

# ndcg10_list

# get nDCG@20 for each system and each query
ndcg20_list = []
n = 20
ig_lists = []

# generate iG list
for i in range(query_number):
    doc_array = qrels_mat[query_id_index==i+1]
    ig_list = [x[2] for x in doc_array]
    
    if len(ig_list) < n:
        for j in range(n-len(ig_list)):
            ig_list.extend([0])
    else:
        ig_list = ig_list[:n]
    
    ig_lists.extend([ig_list])
    
# traverse 6 system
for i in range(system_number):
    # get the array of system for every system 0-6
    system_array = system_results_mat[system_number_index==i+1]
    query_index = np.array([x[1] for x in system_array])
    
    ndcg20_query = []
    # traverse 10 query
    for j in range(query_number):
        # get the array of query for every query 0-10   
        query_array = system_array[query_index==j+1]
        query_n = query_array[:n]
        doc_query_n = [x[2] for x in query_n]
       
        g_list = []
        # get G list 
        for k in range(len(doc_query_n)):
            if doc_query_n[k] in query_rel_doc[j]:
                g_list.extend([doc_rel_dict[j+1][doc_query_n[k]]])
            else:
                g_list.extend([0])
        
        # calculate DCG
        dcg = g_list[0]
        for p in range(len(g_list)-1):
            a = g_list[p + 1] / np.log2(p + 2)
            dcg += a
            
        # calculate iDCG  
        ig_list = ig_lists[j]
        idcg = ig_list[0]
        for q in range(len(ig_list)-1):
            b = ig_list[q + 1] / np.log2(q + 2)
            idcg += b
        
        if idcg == 0 :                    
            ndcg20_query.extend([0])
        else:
            ndcg20_query.extend([dcg / idcg])
            
    ndcg20_list.extend([ndcg20_query])

# ndcg20_list

# generate p10_mean_list r50_mean_list rp_mean_list ap_mean_list ndcg10_mean_list ndcg20_mean_list
p10_mean_list = []; r50_mean_list = []; rp_mean_list = [] 
ap_mean_list = []; ndcg10_mean_list = []; ndcg20_mean_list = []

for i in range(6):
    p10_mean_list.extend([np.mean(p10_list[i])])
    r50_mean_list.extend([np.mean(r50_list[i])])
    rp_mean_list.extend([np.mean(rp_list[i])])
    ap_mean_list.extend([np.mean(ap_list[i])])
    ndcg10_mean_list.extend([np.mean(ndcg10_list[i])])
    ndcg20_mean_list.extend([np.mean(ndcg20_list[i])])


# p10_mean_list


# ### Select the best system (p-value)

# for p10:  best: 3 second: 6
t_p10, p_p10 = scipy.stats.ttest_ind(p10_list[2], p10_list[5])
# for r50:  best: 2 second: 1
t_r50, p_r50 = scipy.stats.ttest_ind(r50_list[1], r50_list[0])
# for r-precision:  best: 3 second: 6
t_rp, p_rp = scipy.stats.ttest_ind(rp_list[2], rp_list[5])
# for AP:  best: 3 second: 6
t_ap, p_ap = scipy.stats.ttest_ind(ap_list[2], ap_list[5])
# for nDCG@10:  best: 3 second: 6
t_ndcg10, p_ndcg10 = scipy.stats.ttest_ind(ndcg10_list[2], ndcg10_list[5])
# for nDCG@20:  best: 3 second: 6
t_ndcg20, p_ndcg20 = scipy.stats.ttest_ind(ndcg20_list[2], ndcg20_list[5])

print('t-statistic of p10: ' + str(t_p10) + '   ' + 'p-value of p10: ' + str(p_p10))
print('t-statistic of r50: ' + str(t_r50) + '   ' + 'p-value of r50: ' + str(p_r50))
print('t-statistic of rp: ' + str(t_rp) + '   ' + 'p-value of rp: ' + str(p_rp))
print('t-statistic of ap: ' + str(t_ap) + '   ' + 'p-value of ap: ' + str(p_ap))
print('t-statistic of ndcg10: ' + str(t_ndcg10) + '   ' + 'p-value of ndcg10: ' + str(p_ndcg10))
print('t-statistic of ndcg20: ' + str(t_ndcg20) + '   ' + 'p-value of ndcg20: ' + str(p_ndcg20))


# ## Generate ir_eval.csv

f = open('ir_eval.csv', 'w', encoding='utf-8')

csv_writer = csv.writer(f)
csv_writer.writerow(['system_number', 'query_number', 'P@10', 'R@50', 'r-precision', 'AP', 'nDCG@10', 'nDCG@20'])

# traverse 6 systems
for i in range(6):
    # traverse 10 queries
    for j in range(10):
        p_10 = round(p10_list[i][j], 3)
        r_50 = round(r50_list[i][j], 3)
        rp = round(rp_list[i][j], 3)
        ap = round(ap_list[i][j], 3)
        ndcg10 = round(ndcg10_list[i][j], 3)
        ndcg20 = round(ndcg20_list[i][j], 3)
        csv_writer.writerow([i+1, j+1, p_10, r_50, rp, ap, ndcg10, ndcg20])
    # write mean
    p10_mean = round(p10_mean_list[i], 3)
    r50_mean = round(r50_mean_list[i], 3)
    rp_mean = round(rp_mean_list[i], 3)
    ap_mean = round(ap_mean_list[i], 3)
    ndcg10_mean = round(ndcg10_mean_list[i], 3)
    ndcg20_mean = round(ndcg20_mean_list[i], 3)
    csv_writer.writerow([i+1, 'mean', p10_mean, r50_mean, rp_mean, ap_mean, ndcg10_mean, ndcg20_mean])




##########################
# #                    # #
# #  2. Text Analysis  # #
# #                    # #
##########################

# ## Preparation

# generate OT NT Quran corpus

with open('train_and_dev.tsv', 'r') as f:
    data = f.readlines()

data_list = []
for d in data:
    a = d.rstrip('\n').split('\t')
    data_list.extend([a])
    
# get ot_list nt_list quran_list
ot_list = []; nt_list = []; quran_list = []
for i in range(len(data_list)):
    if data_list[i][0] == 'OT':
        ot_list.extend([data_list[i][1]])
    elif data_list[i][0] == 'NT':
        nt_list.extend([data_list[i][1]])
    elif data_list[i][0] == 'Quran':
        quran_list.extend([data_list[i][1]])        

print('length of OT: ' + str(len(ot_list)))
print('length of NT: ' + str(len(nt_list)))
print('length of Quran: ' + str(len(quran_list)))

# delete punctuation
# re.sub(pattern, repl, string, count=0, flags=0)
# put words in englishST into a list
with open('englishST.txt', 'r') as eng:
    eng_str = eng.read()
eng_list = eng_str.split()
    
r = """[0-9!#$%&'"()*+,-./:;\\\<=>?@[\]^_`{|}~\n]"""

def preprocessing(s):
    # delete punctuation
    no_punct = re.sub(r, ' ', s)

    # transform to lower_case
    no_punct = no_punct.lower()

    # put string to words list
    no_list = no_punct.split()

    # delete stopping words(englishST.txt) in no_list
    stop_list = []
    for i in no_list:
        if (i not in eng_list):
            stop_list.extend([i])

    # stemming
    norm_list = []
    for i in stop_list:
        norm_list.append(stem(i))
    
    return norm_list

print('length of ot_pre_list: ' + str(len(ot_pre_list)))
print('length of nt_pre_list: ' + str(len(nt_pre_list)))
print('length of quran_pre_list: ' + str(len(quran_pre_list)))

# generate all list
ot_all_list = []; nt_all_list = []; quran_all_list = []

for i in range(len(ot_pre_list)):
    ot_all_list.extend(ot_pre_list[i])
for j in range(len(nt_pre_list)):
    nt_all_list.extend(nt_pre_list[j])
for k in range(len(quran_pre_list)):
    quran_all_list.extend(quran_pre_list[k])

# get corpus dictionary and its key_list

# opeartion for OT
ot_counter = Counter(ot_all_list)
ot_dict = dict(ot_counter)
# operation for NT
nt_counter = Counter(nt_all_list)
nt_dict = dict(nt_counter)
# operation for Quran
quran_counter = Counter(quran_all_list)
quran_dict = dict(quran_counter)

# delete when value < 10
list1 = []; list2 = []
for key, value in ot_dict.items():
    a = 0; b = 0
    if key in nt_dict.keys():
        a = nt_dict[key]
    if key in quran_dict.keys():
        b = quran_dict[key]    
    if a + b + value >= 10:
        list1.append(key)
        list2.append(value)
         
ot_dict = dict(zip(list1,list2))
ot_key = list(ot_dict.keys())

# delete when value < 10
list1 = []; list2 = []
for key, value in nt_dict.items():
    a = 0; b = 0
    if key in ot_dict.keys():
        a = ot_dict[key]
    if key in quran_dict.keys():
        b = quran_dict[key]    
    if a + b + value >= 10:
        list1.append(key)
        list2.append(value)
         
nt_dict = dict(zip(list1,list2))
nt_key = list(nt_dict.keys())

# delete when value < 10
list1 = []; list2 = []
for key, value in quran_dict.items():
    a = 0; b = 0
    if key in ot_dict.keys():
        a = ot_dict[key]
    if key in nt_dict.keys():
        b = nt_dict[key]    
    if a + b + value >= 10:
        list1.append(key)
        list2.append(value)
         
quran_dict = dict(zip(list1,list2))
quran_key = list(quran_dict.keys())

# special word list in OT, NT and Quran
special_list = list(set(ot_key).union(set(nt_key)).union(set(quran_key)))
print('length of ot_key: ' + str(len(ot_key)))
print('length of nt_key: ' + str(len(nt_key)))
print('length of quran_key: ' + str(len(quran_key)))
print('length of special_list: ' + str(len(special_list)))


# ## Compute Mutual Information

# compute mutual information
def takeSecond(elem):
    return elem[1]

N_ot = len(ot_pre_list); N_nt = len(nt_pre_list); N_quran = len(quran_pre_list)
N = N_ot + N_nt + N_quran

# calaculate N N0 N1 N11 N10 N01 N00 for OT

mi_ot_list = []; X2_ot_list = []
N1 = N_ot  # ot
N0 = N - N_ot  # not ot

for w in special_list:
    N11 = 0
    if w in ot_key:  # in ot
        N11 = num_ot_dict[w]          
    N01 = N1 - N11
       
    N10 = 0
    if w in nt_key:  # in nt
        N10 += num_nt_dict[w] 
    if w in quran_key:  # in quran
        N10 += num_quran_dict[w]     
    N00 = N0 - N10
    
    a = 0; b = 0; c = 0; d = 0
    if N11 != 0:
        a = (N11 / N) * np.log2((N * N11)/((N10+N11) * N1))
    if N01 != 0:
        b = (N01 / N) * np.log2((N * N01)/((N00+N01) * N1))
    if N10 != 0:
        c = (N10 / N) * np.log2((N * N10)/((N10+N11) * N0))
    if N00 != 0:
        d = (N00 / N) * np.log2((N * N00)/((N00+N01) * N0))
       
    mi = a + b + c + d
    X2 = ((N11+N10+N01+N00)*(N11*N00-N10*N01)**2) / ((N11+N01)*(N11+N10)*(N10+N00)*(N01+N00))
     
    mi_ot_list.extend([[w, mi]])
    X2_ot_list.extend([[w, X2]])
    
mi_ot_list.sort(key=takeSecond, reverse=True)
X2_ot_list.sort(key=takeSecond, reverse=True)

# calaculate N N0 N1 N11 N10 N01 N00 for NT

mi_nt_list = []; X2_nt_list = []
N1 = N_nt  # nt
N0 = N - N_nt  # not nt

for w in special_list:
    N11 = 0
    if w in nt_key:  # in nt
        N11 = num_nt_dict[w]          
    N01 = N1 - N11
       
    N10 = 0
    if w in ot_key:  # in ot
        N10 += num_ot_dict[w] 
    if w in quran_key:  # in quran
        N10 += num_quran_dict[w]     
    N00 = N0 - N10
    
    a = 0; b = 0; c = 0; d = 0
    if N11 != 0:
        a = (N11 / N) * np.log2((N * N11)/((N10+N11) * N1))
    if N01 != 0:
        b = (N01 / N) * np.log2((N * N01)/((N00+N01) * N1))
    if N10 != 0:
        c = (N10 / N) * np.log2((N * N10)/((N10+N11) * N0))
    if N00 != 0:
        d = (N00 / N) * np.log2((N * N00)/((N00+N01) * N0))
       
    mi = a + b + c + d
    X2 = ((N11+N10+N01+N00)*(N11*N00-N10*N01)**2) / ((N11+N01)*(N11+N10)*(N10+N00)*(N01+N00))
     
    mi_nt_list.extend([[w, mi]])
    X2_nt_list.extend([[w, X2]])
    
mi_nt_list.sort(key=takeSecond, reverse=True)
X2_nt_list.sort(key=takeSecond, reverse=True)

# calaculate N N0 N1 N11 N10 N01 N00 for Quran

mi_quran_list = []; X2_quran_list = []
N1 = N_quran  # quran
N0 = N - N_quran  # not quran

for w in special_list:
    N11 = 0
    if w in quran_key:  # in quran
        N11 = num_quran_dict[w]          
    N01 = N1 - N11
       
    N10 = 0
    if w in ot_key:  # in ot
        N10 += num_ot_dict[w] 
    if w in nt_key:  # in nt
        N10 += num_nt_dict[w]     
    N00 = N0 - N10
    
    a = 0; b = 0; c = 0; d = 0
    if N11 != 0:
        a = (N11 / N) * np.log2((N * N11)/((N10+N11) * N1))
    if N01 != 0:
        b = (N01 / N) * np.log2((N * N01)/((N00+N01) * N1))
    if N10 != 0:
        c = (N10 / N) * np.log2((N * N10)/((N10+N11) * N0))
    if N00 != 0:
        d = (N00 / N) * np.log2((N * N00)/((N00+N01) * N0))
       
    mi = a + b + c + d
    X2 = ((N11+N10+N01+N00)*(N11*N00-N10*N01)**2) / ((N11+N01)*(N11+N10)*(N10+N00)*(N01+N00))
     
    mi_quran_list.extend([[w, mi]])
    X2_quran_list.extend([[w, X2]])
    
mi_quran_list.sort(key=takeSecond, reverse=True)
X2_quran_list.sort(key=takeSecond, reverse=True)

# ## Output MI and X2

# mi_quran_list[:10]

# ## TOPIC-LEVEL COMPARISONS (LDA)

# lda.print_topics()

# get topic score for ot_pre_list:
ot_score_list = []
for i in range(len(ot_pre_list)):
    doc = ot_pre_list[i]
    doc_bow = common_dictionary.doc2bow(doc)
    ot_score_list.extend([lda.get_document_topics(doc_bow, minimum_probability=0)])

# get topic score for nt_pre_list:
nt_score_list = []
for j in range(len(nt_pre_list)):
    doc = nt_pre_list[j]
    doc_bow = common_dictionary.doc2bow(doc)
    nt_score_list.extend([lda.get_document_topics(doc_bow, minimum_probability=0)])

# get topic score for quran_pre_list:
quran_score_list = []
for k in range(len(quran_pre_list)):
    doc = quran_pre_list[k]
    doc_bow = common_dictionary.doc2bow(doc)
    quran_score_list.extend([lda.get_document_topics(doc_bow, minimum_probability=0)])

# get topic average score for ot_pre_list, nt_avg_list and quran_avg_list
num_topic = 20
ot_avg_list = []; nt_avg_list = []; quran_avg_list = [] 

for i in range(num_topic):
    # for ot_avg_list
    ot_score = [x[i][1] for x in ot_score_list]
    ot_avg_score = sum(ot_score) / len(ot_score)
    ot_avg_list.extend([(i, ot_avg_score)])
    # for nt_avg_list
    nt_score = [x[i][1] for x in nt_score_list]
    nt_avg_score = sum(nt_score) / len(nt_score)
    nt_avg_list.extend([(i, nt_avg_score)])
    # for quran_avg_list
    quran_score = [x[i][1] for x in quran_score_list]
    quran_avg_score = sum(quran_score) / len(quran_score)
    quran_avg_list.extend([(i, quran_avg_score)])
    
ot_avg_list.sort(key=takeSecond, reverse=True)
nt_avg_list.sort(key=takeSecond, reverse=True)
quran_avg_list.sort(key=takeSecond, reverse=True)

print('Top 3 topics in OT: ' + str(ot_avg_list[:3]))
print('Top 3 topics in NT: ' + str(nt_avg_list[:3]))
print('Top 3 topics in Quran: ' + str(quran_avg_list[:3]))

print(lda.print_topic(18))
print(lda.print_topic(11))
print(lda.print_topic(12))

print(len(ot_pre_list))
print(len(nt_pre_list))
print(len(quran_pre_list))




##############################
# #                        # #
# # 3. Text Classification # #
# #                        # #
##############################


# ##########################################
# # stemming + stopping
# # Shuffle the order of data
# all_X = ot_pre_list.copy()
# all_X.extend(nt_pre_list)
# all_X.extend(quran_pre_list)

# all_y = []
# for i in range(len(ot_pre_list)):
#     all_y.extend(['ot'])
# for j in range(len(nt_pre_list)):
#     all_y.extend(['nt'])
# for k in range(len(quran_pre_list)):
#     all_y.extend(['quran'])

# # Split the dataset into training set and a seperate development set
# X_training, X_deving, y_training, y_deving = train_test_split(all_X, all_y, train_size=0.9, test_size=0.1, shuffle=True)

# # get vocab set
# vocab_set = set([])

# for doc1 in ot_pre_list:
#     for word1 in doc1:
#         vocab_set.add(word1)
# for doc2 in nt_pre_list:
#     for word2 in doc2:
#         vocab_set.add(word2)
# for doc3 in quran_pre_list:
#     for word3 in doc3:
#         vocab_set.add(word3)
# ##########################################

# tokenisation
r = """[0-9!#$%&'"()*+,-./:;\\\<=>?@[\]^_`{|}~\n]"""

def tokenising(s):
    # delete punctuation
    no_punct = re.sub(r, ' ', s)

    # transform to lower_case
    no_punct = no_punct.lower()

    # put string to words list
    no_list = no_punct.split()
    
    return no_list

print('length of ot_token_list: ' + str(len(ot_token_list)))
print('length of nt_token_list: ' + str(len(nt_token_list)))
print('length of quran_token_list: ' + str(len(quran_token_list)))

# get vocab set
vocab_set = set([])

for doc1 in ot_token_list:
    for word1 in doc1:
        vocab_set.add(word1)
for doc2 in nt_token_list:
    for word2 in doc2:
        vocab_set.add(word2)
for doc3 in quran_token_list:
    for word3 in doc3:
        vocab_set.add(word3)

# Shuffle the order of data
all_X = ot_token_list.copy()
all_X.extend(nt_token_list)
all_X.extend(quran_token_list)

all_y = []
for i in range(len(ot_token_list)):
    all_y.extend(['ot'])
for j in range(len(nt_token_list)):
    all_y.extend(['nt'])
for k in range(len(quran_token_list)):
    all_y.extend(['quran'])

# Split the dataset into training set and a seperate development set
X_training, X_deving, y_training, y_deving = train_test_split(all_X, all_y, train_size=0.9, test_size=0.1, shuffle=True)


# ### Process test data

# ########################################################
# # stemming + stopping
# with open('test.tsv', 'r') as f:
#     test = f.readlines()

# test_list = []
# for d in test:
#     a = d.rstrip('\n').split('\t')
#     test_list.extend([a])
    
# # get X_testing and y_testing
# X_testing = []; y_testing = []
# for i in range(len(test_list)):
#     line = preprocessing(test_list[i][1])
#     if len(line) > 0:
#         X_testing.extend([line])
#         y_testing.extend([test_list[i][0].lower()])
# ########################################################

# len(X_testing)

# read test.tsv file
with open('test.tsv', 'r') as f:
    test = f.readlines()

test_list = []
for d in test:
    a = d.rstrip('\n').split('\t')
    test_list.extend([a])
    
# get doc_list and label_list
doc_list = []; label_list = []
for i in range(len(test_list)):
    doc_list.extend([test_list[i][1]])
    label_list.extend([test_list[i][0]])

# tokenise doc_list and get X_testing and y_testing
X_testing = []; y_testing = []
for i in range(len(doc_list)):
    line = tokenising(doc_list[i])
    if (len(line) > 0):
        X_testing.extend([line])
        y_testing.extend([label_list[i].lower()])

len(y_testing)


# ### Set up mappings for word and category IDs

# convert the vocab to a word id lookup dictionary
# anything not in this will be considered "out of vocabulary" OOV
word2id = {}
for word_id,word in enumerate(vocab_set):
    word2id[word] = word_id
    
# and do the same for the categories
categories = ['ot', 'nt', 'quran']
cat2id = {}
for cat_id,cat in enumerate(set(categories)):
    cat2id[cat] = cat_id
    
print("The word id for dog is",word2id['dog'])
print("The category id for nt is",cat2id['nt'])


# ### Convert data to bag-of-words format

# build a BOW representation of the files: use the scipy 
# data is the preprocessed_data
# word2id maps words to their ids
def convert_to_bow_matrix(preprocessed_data, word2id):
    
    # matrix size is number of docs x vocab size + 1 (for OOV)
    matrix_size = (len(preprocessed_data),len(word2id)+1)
    oov_index = len(word2id)
    # matrix indexed by [doc_id, token_id]
    X = scipy.sparse.dok_matrix(matrix_size)

    # iterate through all documents in the dataset
    for doc_id,doc in enumerate(preprocessed_data):
        for word in doc:
            # default is 0, so just add to the count for this word in this doc
            # if the word is oov, increment the oov_index
            X[doc_id,word2id.get(word,oov_index)] += 1
    
    return X

# check some docs
# print("First 3 documents in X_train are:",X_train[:3])
# print("First 3 documents in y_train are:",y_train[:3])


# ### Train an SVM model

# ### Evaluating the model (using train set)

# evaluate on training data: how well did we fit to the data we trained on?
y_train_predictions = model.predict(X_train)

# now can compute any metrics we care about. Let's quickly do accuracy
def compute_accuracy(predictions, true_values):
    num_correct = 0
    num_total = len(predictions)
    for predicted,true in zip(predictions, true_values):
        if predicted==true:
            num_correct += 1
    return num_correct / num_total

accuracy = compute_accuracy(y_train_predictions, y_train)
print("Accuracy:",accuracy)

cat_names = []
for cat,cid in sorted(cat2id.items(),key=lambda x:x[1]):
    cat_names.append(cat)

# compute the precision, recall and f1-score for train
print(classification_report(y_train, y_train_predictions, target_names=cat_names, digits=3))

# ### Using dev set

def compute_abc_accuracy(predictions, true_values):
    num_correct = 0
    error_num = 0
    num_total = len(predictions)
    for predicted,true in zip(predictions, true_values):
        if predicted==true:
            num_correct += 1
        else:
            print(error_num)
        error_num += 1
        
    return num_correct / num_total

# prepare dev data in the same was as training data
X_dev = convert_to_bow_matrix(X_deving, word2id)
y_dev = [cat2id[cat] for cat in y_deving]

y_dev_predictions = model.predict(X_dev)
accuracy = compute_accuracy(y_dev_predictions, y_dev)
print("Accuracy:", accuracy)

# compute the precision, recall and f1-score for dev
print(classification_report(y_dev, y_dev_predictions, target_names=cat_names, digits=3))


# ### Using test set

# prepare dev data in the same was as training data
X_test = convert_to_bow_matrix(X_testing, word2id)
y_test = [cat2id[cat] for cat in y_testing]

y_test_predictions = model.predict(X_test)
accuracy = compute_accuracy(y_test_predictions, y_test)
print("Accuracy:", accuracy)

# compute the precision, recall and f1-score for dev
print(classification_report(y_test, y_test_predictions, target_names=cat_names, digits=3))


# ## Improved Model

# generate df dictionary
N = len(X_training)
df_dict = {}
for w in vocab_set:
    df_dict[w] = 0
df_set = set(df_dict.keys())

for i in range(len(X_training)):
    words = set(X_training[i])
    for word in words:
        if word in df_set:
            df_dict[word] += 1

def convert_to_tfidf_matrix(preprocessed_data, word2id):
    
    # matrix size is number of docs x vocab size + 1 (for OOV)
    matrix_size = (len(preprocessed_data),len(word2id))

    # matrix indexed by [doc_id, token_id]
    X = scipy.sparse.dok_matrix(matrix_size)

    # iterate through all documents in the dataset
    for doc_id,doc in enumerate(preprocessed_data):
        for word in doc:
            # default is 0, so just add to the count for this word in this doc
            if word in df_set:
                X[doc_id, word2id.get(word)] += 1
        for word in doc:
            if word in df_set:
                a = (1 + np.log10(X[doc_id, word2id.get(word)])) * np.log10(N / df_dict[word])
                X[doc_id, word2id.get(word)] = a
    
    return X


# ### Train a SVM Model (improved)

# ### Evaluating the improved Model (using train set)

# evaluate on training data: how well did we fit to the data we trained on?
y_train_improved_predictions = improved_model.predict(X_train_improved)
accuracy = compute_accuracy(y_train_improved_predictions, y_train_improved)
print("Accuracy:",accuracy)

# compute the precision, recall and f1-score for train
print(classification_report(y_train_improved, y_train_improved_predictions, target_names=cat_names, digits=3))


# ### Using dev set

# prepare dev data in the same was as training data
tf_transformer2 = TfidfTransformer(use_idf=False).fit(X_dev)
X_dev_improved = tf_transformer2.transform(X_dev)
y_dev_improved = [cat2id[cat] for cat in y_deving]
# X_dev_improved = convert_to_tfidf_matrix(X_deving, word2id)
# y_dev_improved = [cat2id[cat] for cat in y_deving]

y_dev_improved_predictions = improved_model.predict(X_dev_improved)
accuracy = compute_accuracy(y_dev_improved_predictions, y_dev_improved)
print("Accuracy:", accuracy)

# compute the precision, recall and f1-score for dev
print(classification_report(y_dev_improved, y_dev_improved_predictions, target_names=cat_names, digits=3))


# ### Using test set

# prepare dev data in the same was as training data
tf_transformer3 = TfidfTransformer(use_idf=False).fit(X_test)
X_test_improved = tf_transformer2.transform(X_test)
y_test_improved = [cat2id[cat] for cat in y_testing]

y_test_improved_predictions = improved_model.predict(X_test_improved)
accuracy = compute_accuracy(y_test_improved_predictions, y_test_improved)
print("Accuracy:", accuracy)

# compute the precision, recall and f1-score for dev
print(classification_report(y_test_improved, y_test_improved_predictions, target_names=cat_names, digits=3))


# # Output csv

f = open('classification.csv', 'w', encoding='utf-8')

csv_writer = csv.writer(f)
csv_writer.writerow(['system', 'split', 'p-quran', 'r-quran', 'f-quran', 'p-ot', 'r-ot', 'f-ot', 'p-nt', 'r-nt', 'f-nt', 'p-macro', 'r-macro', 'f-macro'])

csv_writer.writerow(['baseline', 'train', 0.998, 0.994, 0.996, 0.997, 0.991, 0.994, 0.972, 0.993, 0.982, 0.989, 0.992, 0.991])
csv_writer.writerow(['baseline', 'dev', 0.826, 0.852, 0.838, 0.917, 0.874, 0.895, 0.714, 0.792, 0.751, 0.819, 0.839, 0.828])
csv_writer.writerow(['baseline', 'test', 0.843, 0.831, 0.837, 0.921, 0.888, 0.904, 0.744, 0.827, 0.783, 0.836, 0.848, 0.841])

csv_writer.writerow(['improved', 'train', 1.000, 1.000, 1.000, 1.000, 1.000, 1.000, 1.000, 1.000, 1.000, 1.000, 1.000, 1.000])
csv_writer.writerow(['improved', 'dev', 0.933, 0.903, 0.918, 0.878, 0.825, 0.851, 0.934, 0.960, 0.947, 0.915, 0.896, 0.905])
csv_writer.writerow(['improved', 'test', 0.924, 0.900, 0.912, 0.881, 0.832, 0.856, 0.928, 0.953, 0.940, 0.911, 0.895, 0.902])


