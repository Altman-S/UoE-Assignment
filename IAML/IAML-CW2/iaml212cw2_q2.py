
##########################################################
#  Python script template for Question 2 (IAML Level 11)
#  Note that
#  - You should not change the filename of this file, 'iaml212cw2_q2.py', which is the file name you should use when you submit your code for this question.
#  - You should write code for the functions defined below. Do not change their names.
#  - You can define function arguments (parameters) and returns (attributes) if necessary.
#  - In case you define helper functions, do not define them here, but put them in a separate Python module file, "iaml212cw2_my_helpers.py", and import it in this script.
#  - For those questions requiring you to show results in tables, your code does not need to present them in tables - just showing them with print() is fine.
#  - You do not need to include this header in your submission.
##########################################################

#--- Code for loading modules and the data set and pre-processing --->
# NB: You can edit the following and add code (e.g. code for loading sklearn) if necessary.

import numpy as np
import scipy
import matplotlib.pyplot as plt
import seaborn as sns
from sklearn.cluster import KMeans
from sklearn.linear_model import LogisticRegression
from scipy.stats import multivariate_normal
from sklearn.mixture import GaussianMixture
from iaml212cw2_helpers import *
from iaml212cw2_my_helpers import *

#<----

Xtrn_org, Ytrn_org, Xtst_org, Ytst_org = load_Q2_dataset()
Xtrn = Xtrn_org / 255.0
Xtst = Xtst_org / 255.0
Ytrn = Ytrn_org - 1
Ytst = Ytst_org - 1
Xmean = np.mean(Xtrn, axis=0)
Xtrn_m = Xtrn - Xmean; Xtst_m = Xtst - Xmean  # Mean-normalised versions

print('Xtrn shape: ' + str(Xtrn.shape) + '   Ytrn shape: ' + str(Ytrn.shape))


# Q2.1
def iaml212cw2_q2_1():
    # Q2.1 a
    min_trn = np.min(Xtrn)
    max_trn = np.max(Xtrn)
    mean_trn = np.mean(Xtrn)
    std_trn = np.std(Xtrn)
    min_tst = np.min(Xtst)
    max_tst = np.max(Xtst)
    mean_tst = np.mean(Xtst)
    std_tst = np.std(Xtst)

    print('Train: min: ' + str(min_trn) + '   max: ' + str(max_trn) + '   mean: ' + str(mean_trn) + '   std: ' + str(std_trn))
    print('Test: min: ' + str(min_tst) + '   max: ' + str(max_tst) + '   mean: ' + str(mean_tst) + '   std: ' + str(std_tst))

    # Q2.1 b
    pic1 = np.reshape(Xtrn[0, :], newshape=(28, 28))
    pic2 = np.reshape(Xtrn[1, :], newshape=(28, 28))

    plt.imshow(pic1, cmap='gray_r')
    plt.show()
    plt.imshow(pic2, cmap='gray_r')
    plt.show()

# iaml212cw2_q2_1()


# Q2.2
# def iaml212cw2_q2_2():
#
# iaml212cw2_q2_2()


# Q2.3
def iaml212cw2_q2_3():
    # generate the instances of class 0, 5 and 8 (i.e. ’A’, ’F’, ’I’) in Xtrn
    X = []; X_0 = []; X_5 = []; X_8 = []
    y = []; y_0 = []; y_5 = []; y_8 = []
    for i in range(len(Ytrn)):
        if (Ytrn[i] == 0):
            X_0.extend([Xtrn[i]])
            y_0.extend([0])
        elif (Ytrn[i] == 5):
            X_5.extend([Xtrn[i]])
            y_5.extend([5])
        elif (Ytrn[i] == 8):
            X_8.extend([Xtrn[i]])
            y_8.extend([8])
    X.extend([X_0]); y.extend([y_0])
    X.extend([X_5]); y.extend([y_5])
    X.extend([X_8]); y.extend([y_8])

    # set up KMeans (k = 3)
    km3 = KMeans(n_clusters=3, random_state=0)
    img3_list = []
    for i in range(len(X)):
        km3.fit(X[i])
        img_list = km3.cluster_centers_
        img3_list.extend([img_list])
    print(len(img3_list[0]))

    # set up KMeans (k = 5)
    km5 = KMeans(n_clusters=5, random_state=0)
    img5_list = []
    for i in range(len(X)):
        km5.fit(X[i])
        img_list = km5.cluster_centers_
        img5_list.extend([img_list])
    print(len(img5_list[0]))

    # plot for k = 3
    fig1 = plt.figure()
    for i in range(len(img3_list)):
        for j in range(len(img3_list[0])):
            pic = np.reshape(img3_list[i][j], newshape=(28, 28))
            ax1 = fig1.add_subplot(3, 3, i*3+j+1)
            ax1.imshow(pic, cmap='gray_r')
    fig1.show()

    # plot for k = 5
    fig2 = plt.figure()
    for i in range(len(img5_list)):
        for j in range(len(img5_list[0])):
            pic = np.reshape(img5_list[i][j], newshape=(28, 28))
            ax2 = fig2.add_subplot(3, 5, i*5+j+1)
            ax2.imshow(pic, cmap='gray_r')
    fig2.show()

# iaml212cw2_q2_3()


# Q2.5
def iaml212cw2_q2_5():
    # a: classification accuracy for each of the training set and test set
    lr = LogisticRegression(max_iter=1000, random_state=0)
    lr.fit(Xtrn_m, Ytrn)
    acc_train = lr.score(Xtrn_m, Ytrn)
    acc_test = lr.score(Xtst_m, Ytst)
    print('Classification accuracy on training set: {:.6f}'.format(acc_train))
    print('Classification accuracy on testing set: {:.6f}'.format(acc_test))

    # b: top ﬁve classes that were misclassfied most in the test set
    alphabet_list = [['A', 0], ['B', 0], ['C', 0], ['D', 0], ['E', 0], ['F', 0], ['G', 0], ['H', 0], ['I', 0],
                     ['J', 0], ['K', 0], ['L', 0], ['M', 0], ['N', 0], ['O', 0], ['P', 0], ['Q', 0], ['R', 0],
                     ['S', 0], ['T', 0], ['U', 0], ['V', 0], ['W', 0], ['X', 0], ['Y', 0], ['Z', 0]]

    Ypred = lr.predict(Xtst_m)
    for i in range(len(Ytst)):
        if (Ypred[i] == Ytst[i]):
            alphabet_list[Ytst[i]][1] += 1
    alphabet_list.sort(key=takeSecond)
    # print(alphabet_list)

    error_list = alphabet_list
    for i in range(len(error_list)):
        error_list[i][1] = 100 - error_list[i][1]

    print('the numbers of misclassifications: ' + str(error_list))

# iaml212cw2_q2_5()


# Q2.6
def iaml212cw2_q2_6():
    # C penalty tol solver=saga
    # a: classification accuracy for each of the training set and test set
    lr = LogisticRegression(C=1000, penalty='l1', tol=0.1, solver='saga', max_iter=1000, random_state=0)
    lr.fit(Xtrn_m, Ytrn)
    acc_train = lr.score(Xtrn_m, Ytrn)
    acc_test = lr.score(Xtst_m, Ytst)
    print('Classification accuracy on training set: {:.6f}'.format(acc_train))
    print('Classification accuracy on testing set: {:.6f}'.format(acc_test))

    # b: top ﬁve classes that were misclassfied most in the test set
    alphabet_list = [['A', 0], ['B', 0], ['C', 0], ['D', 0], ['E', 0], ['F', 0], ['G', 0], ['H', 0], ['I', 0],
                     ['J', 0], ['K', 0], ['L', 0], ['M', 0], ['N', 0], ['O', 0], ['P', 0], ['Q', 0], ['R', 0],
                     ['S', 0], ['T', 0], ['U', 0], ['V', 0], ['W', 0], ['X', 0], ['Y', 0], ['Z', 0]]

    Ypred = lr.predict(Xtst_m)
    for i in range(len(Ytst)):
        if (Ypred[i] == Ytst[i]):
            alphabet_list[Ytst[i]][1] += 1
    alphabet_list.sort(key=takeSecond)
    # print(alphabet_list)

    num_good = 0
    error_list = alphabet_list
    for i in range(len(error_list)):
        num_good += error_list[i][1]
        error_list[i][1] = 100 - error_list[i][1]

    print('the numbers of misclassifications: ' + str(error_list))
    print('accuracy: ' + str(num_good/2600))

iaml212cw2_q2_6()


# Q2.7
def iaml212cw2_q2_7():
    X_A = Xtrn_m[Ytrn == 0]

    # a: minimum, maximum, and mean values of the diagonal elements of the covariance matrix
    cov_mat = np.cov(X_A, rowvar=False)
    min_cov = np.min(cov_mat)
    max_cov = np.max(cov_mat)
    mean_cov = np.mean(cov_mat)

    print('min_cov: ' + str(min_cov))
    print('max_cov: ' + str(max_cov))
    print('mean_cov: ' + str(mean_cov))

    # b: minimum, maximum, and mean values of the diagonal elements of the covariance matrix.
    diagonal_list = []
    for i in range(len(cov_mat)):
        diagonal_list.extend([cov_mat[i][i]])

    min_diag = np.min(diagonal_list)
    max_diag = np.max(diagonal_list)
    mean_diag = np.mean(diagonal_list)

    print('min_diag: ' + str(min_diag))
    print('max_diag: ' + str(max_diag))
    print('mean_diag: ' + str(mean_diag))

    # c: show the histogram of the diagonal values of the covariance matrix
    plt.hist(diagonal_list, bins=15)
    plt.grid()
    plt.show()

    # d: calculate the likelihood of the first element of class 0 in Xtst_m
    # 奇异矩阵？？？
    mean_vec = np.mean(X_A, axis=0)

    # likelihood = multivariate_normal.pdf(Xtst_m, mean=mean_vec, cov=cov_mat, allow_singular=True)
    likelihood = multivariate_normal.pdf(Xtst_m, mean=mean_vec, cov=cov_mat)
    print(likelihood)

# iaml212cw2_q2_7()


# Q2.8
def iaml212cw2_q2_8():
    # a: data of class 0
    X_train0 = Xtrn_m[Ytrn == 0]
    gm = GaussianMixture(n_components=1, covariance_type='full')
    gm.fit(X_train0)

    log_likelihood = gm.score([Xtst_m[0]])
    print('log_likelihood: ' + str(log_likelihood))

    # b: classiﬁcation experiment considering all the 26 classes
    # set up 26 classifiers
    gm_list = []
    for i in range(26):
        gm = GaussianMixture(n_components=1, covariance_type='full')
        gm.fit(Xtrn_m[Ytrn == i])
        gm_list.extend([gm])

    # Use {Xtrn_m, Ytrn}
    score_list = []
    for i in range(26):
        score = gm_list[i].score_samples(Xtrn_m)
        score_list.extend([score])
    Ytrn_pred = np.argmax(score_list, axis=0)
    num_acc_trn = 0
    for i in range(len(Ytrn)):
        if (Ytrn[i] == Ytrn_pred[i]):
            num_acc_trn += 1

    print('number of correctly classified instances in training: ' + str(num_acc_trn))
    print('classification accuracy in training: ' + str(num_acc_trn / len(Ytrn)))

    # Use {Xtst_m, Ytst}
    score_list2 = []
    for i in range(26):
        score2 = gm_list[i].score_samples(Xtst_m)
        score_list2.extend([score2])
    Ytst_pred = np.argmax(score_list2, axis=0)
    num_acc_tst = 0
    for i in range(len(Ytst)):
        if (Ytst[i] == Ytst_pred[i]):
            num_acc_tst += 1

    print('number of correctly classified instances in testing: ' + str(num_acc_tst))
    print('classification accuracy in testing: ' + str(num_acc_tst/len(Ytst)))

# iaml212cw2_q2_8()


# Q2.9
# def iaml212cw2_q2_9():
#
# iaml212cw2_q2_9()   # comment this out when you run the function


# Q2.10
def iaml212cw2_q2_10():
    # # Q2.10 a
    # k_list = [1, 2, 4, 8]
    # acc_training_list = []
    # acc_testing_list = []
    #
    # for k in k_list:
    #     # set up 26 classifiers for k
    #     gm_list = []
    #     for i in range(26):
    #         gm = GaussianMixture(n_components=k, covariance_type='full')
    #         gm.fit(Xtrn_m[Ytrn == i])
    #         gm_list.extend([gm])
    #
    #     # Use {Xtrn_m, Ytrn}
    #     score_list = []
    #     for i in range(26):
    #         score = gm_list[i].score_samples(Xtrn_m)
    #         score_list.extend([score])
    #     Ytrn_pred = np.argmax(score_list, axis=0)
    #     num_acc_trn = 0
    #     for i in range(len(Ytrn)):
    #         if (Ytrn[i] == Ytrn_pred[i]):
    #             num_acc_trn += 1
    #
    #     # print('number of correctly classified instances in training: ' + str(num_acc_trn))
    #     # print('classification accuracy in training: ' + str(num_acc_trn / len(Ytrn)))
    #     acc_training_list.extend([num_acc_trn / len(Ytrn)])
    #
    #     # Use {Xtst_m, Ytst}
    #     score_list2 = []
    #     for i in range(26):
    #         score2 = gm_list[i].score_samples(Xtst_m)
    #         score_list2.extend([score2])
    #     Ytst_pred = np.argmax(score_list2, axis=0)
    #     num_acc_tst = 0
    #     for i in range(len(Ytst)):
    #         if (Ytst[i] == Ytst_pred[i]):
    #             num_acc_tst += 1
    #
    #     # print('number of correctly classified instances in testing: ' + str(num_acc_tst))
    #     # print('classification accuracy in testing: ' + str(num_acc_tst/len(Ytst)))
    #     acc_testing_list.extend([num_acc_tst / len(Ytst)])
    #
    # print('acc_training_list: ' + str(acc_training_list))
    # print('acc_testing_list: ' + str(acc_testing_list))

    # Q2.10 b
    # set up 26 classifiers
    gm_list = []
    for i in range(26):
        gm = GaussianMixture(n_components=2, covariance_type='full', reg_covar=0.1)
        gm.fit(Xtrn_m[Ytrn == i])
        gm_list.extend([gm])

    # Use {Xtst_m, Ytst}
    score_list = []
    for i in range(26):
        score = gm_list[i].score_samples(Xtst_m)
        score_list.extend([score])
    Ytst_pred = np.argmax(score_list, axis=0)
    num_acc_tst = 0
    for i in range(len(Ytst)):
        if (Ytst[i] == Ytst_pred[i]):
            num_acc_tst += 1

    print('classification accuracy in testing: ' + str(num_acc_tst/len(Ytst)))


# iaml212cw2_q2_10()

