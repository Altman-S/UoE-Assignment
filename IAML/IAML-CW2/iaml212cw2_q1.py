
##########################################################
#  Python script template for Question 1 (IAML Level 11)
#  Note that
#  - You should not change the name of this file, 'iaml212cw2_q1.py', which is the file name you should use when you submit your code for this question.
#  - You should write code for the functions defined below. Do not change their names.
#  - You can define function arguments (parameters) and returns (attributes) if necessary.
#  - In case you define additional functions, do not define them here, but put them in a separate Python module file, "iaml212cw2_my_helpers.py", and import it in this script.
#  - For those questions requiring you to show results in tables, your code does not need to present them in tables - just showing them with print() is fine.
#  - You do not need to include this header in your submission.
##########################################################

#--- Code for loading modules and the data set and pre-processing --->
# NB: You can edit the following and add code (e.g. code for loading sklearn) if necessary.

import numpy as np
import scipy
import matplotlib.pyplot as plt
import seaborn as sns
import pandas as pd
import math
from sklearn import decomposition
from sklearn.preprocessing import StandardScaler
from sklearn.svm import SVC
from sklearn.model_selection import StratifiedKFold
from sklearn.linear_model import LogisticRegression
from scipy.stats import gaussian_kde, multivariate_normal
from iaml212cw2_helpers import *
from iaml212cw2_my_helpers import *

#<----

X, Y = load_Q1_dataset()
print('X:', X.shape, 'Y:', Y.shape)
Xtrn = X[100:, :]; Ytrn = Y[100:]  # train dataset
Xtst = X[0:100, :]; Ytst = Y[0:100]  # test dataset

scaler = StandardScaler().fit(Xtrn)
Xtrn_s = scaler.transform(Xtrn)  # standardised training data
Xtst_s = scaler.transform(Xtst)  # standardised testing data


# Q1.1
def iaml212cw2_q1_1():
    plt.subplots(nrows=3, ncols=3)

    for i in range(9):  # plot 9 pictures
        Xa = []
        Xb = []
        for j in range(len(Ytrn)):
            if (Ytrn[j] == 0):
                Xa.extend([Xtrn[j][i]])  # instances of class 0
            else:
                Xb.extend([Xtrn[j][i]])  # instances of class 1

        plt.subplot(3, 3, i + 1)
        plt.title('A' + str(i))
        plt.xlabel('Value')
        plt.ylabel('Number')
        plt.hist([Xa, Xb], bins=15, label=['0', '1'])
        plt.legend(loc='upper right', fontsize='x-small')
        plt.grid()

    plt.show()

# iaml212cw2_q1_1()


# Q1.2
def iaml212cw2_q1_2():
    coef_list = []  # store 9 correlation coefficients
    y = Ytrn
    y_pd = pd.Series(y)  # transform them to pd.Series

    for i in range(9):  # calculate 9 coefficients
        x = [row[i] for row in Xtrn]
        x_pd = pd.Series(x)
        corr_coef = round(x_pd.corr(y_pd), 4)  # invoke correlation coefficient formula
        coef_list.extend([corr_coef])

    print('coef_list: ' + str(coef_list))

# iaml212cw2_q1_2()


# Q1.3
# def iaml212cw2_q1_3():
#
# iaml212cw2_q1_3()   # comment this out when you run the function


# Q1.4
def iaml212cw2_q1_4():
    N = len([x[0] for x in Xtrn])  # length of each attribute of Xtrn
    var_sorted_list = []  # variance sorted list
    sumofvar = 0.0

    for i in range(9):
        col_i = [row[i] for row in Xtrn]
        avg = sumOfList(col_i) / N  # average of an attribute
        square = 0
        for j in range(len(col_i)):
            square = square + math.pow((Xtrn[j][i]-avg),2)
        variance = float(format(square / (N-1), '.4f'))
        sumofvar = sumofvar + variance

        var_sorted_list.extend([[i, variance]])

    var_sorted_list.sort(key=takeSecond, reverse=True)

    print('var_list: ' + str(var_sorted_list))
    print('sumofvar: ' + str(sumofvar))

    # plot 2 pictures
    var_tick_labels = []  # preparing data for plot 1
    y_var = []
    for i in range(len(var_sorted_list)):  # generate attributes' labels
        s = 'A' + str(var_sorted_list[i][0])
        var_tick_labels.extend([s])
        y_var.extend([var_sorted_list[i][1]])
    x_attr = np.arange(len(var_tick_labels))

    cum_explained_var = []  # preparing data for plot 2
    for i in range(len(var_sorted_list)):
        if i == 0:
            explained_var = var_sorted_list[i][1] / sumofvar
            cum_explained_var.append(float(format(explained_var, '.4f')))
        else:
            explained_var = var_sorted_list[i][1] / sumofvar + cum_explained_var[i - 1]
            cum_explained_var.append(float(format(explained_var, '.4f')))
    print('cum_var_ratio: ' + str(cum_explained_var))
    ratio_tick_labels = [1, 2, 3, 4, 5, 6, 7, 8, 9]
    x_attr2 = np.arange(len(ratio_tick_labels))
    y_ratio = cum_explained_var

    # plot 2 pictures side by side
    plt.subplots(nrows=1, ncols=2)
    # plot variance-attribute picture
    plt.subplot(1, 2, 1)
    plt.bar(x_attr, y_var, 0.5, align="center", color="c")
    plt.xlabel('Attribute')
    plt.ylabel('Variance')
    plt.xticks(x_attr, var_tick_labels)
    plt.grid()
    # plot ratio-number of attribute picture
    plt.subplot(1, 2, 2)
    plt.bar(x_attr2, y_ratio, 0.5, align="center", color="g")
    plt.xlabel('Number of Attribute')
    plt.ylabel('Cumulative variance ratio')
    plt.xticks(x_attr2, ratio_tick_labels)
    plt.ylim([0, 1])
    plt.grid()

    plt.show()

# iaml212cw2_q1_4()


# Q1.5
def iaml212cw2_q1_5():
    # fit PCA model
    df = pd.DataFrame({'A0': [x[0] for x in Xtrn], 'A1': [x[1] for x in Xtrn], 'A2': [x[2] for x in Xtrn],
                       'A3': [x[3] for x in Xtrn], 'A4': [x[4] for x in Xtrn], 'A5': [x[5] for x in Xtrn],
                       'A6': [x[6] for x in Xtrn], 'A7': [x[7] for x in Xtrn], 'A8': [x[8] for x in Xtrn]})
    pca = decomposition.PCA()
    pca.fit(df)

    # Q1.5 a
    var_list = pca.explained_variance_  # unbiased sample variance
    sumofvar = 0.0
    print('var_list: ' + str(var_list))

    for i in range(len(var_list)):
        sumofvar = sumofvar + var_list[i]
    print('sumofvar: ' + str(sumofvar))

    # Q1.5 b
    cum_explained_var = pca.explained_variance_ratio_.cumsum()
    print('cum_var_ratio: ' + str(cum_explained_var))

    # plot 2 pictures
    var_tick_labels = []
    y_var = []
    for i in range(len(var_list)):  # generate attributes' labels
        s = 'pc' + str(i+1)
        var_tick_labels.extend([s])
        y_var.extend([var_list[i]])  # preparing data for plot 1
    x_attr = np.arange(len(var_tick_labels))

    ratio_tick_labels = [1, 2, 3, 4, 5, 6, 7, 8, 9]
    x_attr2 = np.arange(len(ratio_tick_labels))
    y_ratio = cum_explained_var  # preparing data for plot 2

    # plot 2 pictures side by side
    plt.subplots(nrows=1, ncols=2)
    # plot variance-attribute picture
    plt.subplot(1, 2, 1)
    plt.bar(x_attr, y_var, 0.5, align="center", color="c")
    plt.xlabel('Attribute')
    plt.ylabel('Variance')
    plt.xticks(x_attr, var_tick_labels)
    plt.grid()
    # plot ratio-number of attribute picture
    plt.subplot(1, 2, 2)
    plt.bar(x_attr2, y_ratio, 0.5, align="center", color="g")
    plt.xlabel('Number of Attribute')
    plt.ylabel('Cumulative variance ratio')
    plt.xticks(x_attr2, ratio_tick_labels)
    plt.ylim([0, 1])
    plt.grid()
    plt.show()

    # Q1.5 c
    # ﬁrst two principal components
    pc1 = pca.components_[0]
    pc2 = pca.components_[1]
    x0_list = []  # store class0 point
    y0_list = []
    x1_list = []  # store class1 point
    y1_list = []

    for i in range(len(Ytrn)):
        x = productSum(pc1, Xtrn[i][:])
        y = productSum(pc2, Xtrn[i][:])
        if (Ytrn[i] == 0):
            x0_list.extend([x])
            y0_list.extend([y])
        elif(Ytrn[i] == 1):
            x1_list.extend([x])
            y1_list.extend([y])

    # plot PCA scatter
    plt.scatter(x0_list, y0_list, label='class0', color='blue')
    plt.scatter(x1_list, y1_list, label='class1', color='red')
    plt.xlabel('PC1')
    plt.ylabel('PC2')
    plt.title('PCA Scatter Plot')
    plt.legend(labels=['class0','class1'] , loc='upper right')
    plt.grid()
    plt.show()

    # Q1.5 d
    pcs = []
    x_axises = []
    corr_coefs = []
    corr_coefs_matrix = []
    pcs.extend([pc1])
    pcs.extend([pc2])
    # generate x1-pc1 x2-pc2
    for i in range(len(pcs)):
        x_axis = []
        for j in range(len(Xtrn)):
            x = productSum(pcs[i], Xtrn[j])
            x_axis.extend([x])
        x_axises.extend([x_axis])

    # generate correlation coefficient matrix
    for i in range(len(pcs)):
        corr_coef = []
        for j in range(Xtrn.shape[1]):
            coef = np.corrcoef(x_axises[i], [x[j] for x in Xtrn])
            corr_coef.extend([coef])
        corr_coefs_matrix.extend([corr_coef])

    # generate correlation coefficient
    for line in corr_coefs_matrix:
        corr = []
        for i in range(len(line)):
            corr.extend([line[i][0][1]])
        corr_coefs.extend([corr])

    print('corr_coefs: ' + str(corr_coefs))

# iaml212cw2_q1_5()


# Q1.6
def iaml212cw2_q1_6():
    # fit PCA model
    df = pd.DataFrame({'A0': [x[0] for x in Xtrn_s], 'A1': [x[1] for x in Xtrn_s], 'A2': [x[2] for x in Xtrn_s],
                       'A3': [x[3] for x in Xtrn_s], 'A4': [x[4] for x in Xtrn_s], 'A5': [x[5] for x in Xtrn_s],
                       'A6': [x[6] for x in Xtrn_s], 'A7': [x[7] for x in Xtrn_s], 'A8': [x[8] for x in Xtrn_s]})
    pca = decomposition.PCA()
    pca.fit(df)

    # Q1.6 a
    var_list = pca.explained_variance_  # unbiased sample variance
    sumofvar = 0.0
    print('var_list: ' + str(var_list))

    for i in range(len(var_list)):
        sumofvar = sumofvar + var_list[i]
    print('sumofvar: ' + str(sumofvar))

    # Q1.6 b
    cum_explained_var = pca.explained_variance_ratio_.cumsum()
    print('cum_var_ratio: ' + str(cum_explained_var))

    # plot 2 pictures
    var_tick_labels = []
    y_var = []
    for i in range(len(var_list)):  # generate attributes' labels
        s = 'pc' + str(i + 1)
        var_tick_labels.extend([s])
        y_var.extend([var_list[i]])  # preparing data for plot 1
    x_attr = np.arange(len(var_tick_labels))

    ratio_tick_labels = [1, 2, 3, 4, 5, 6, 7, 8, 9]
    x_attr2 = np.arange(len(ratio_tick_labels))
    y_ratio = cum_explained_var  # preparing data for plot 2

    # plot 2 pictures side by side
    plt.subplots(nrows=1, ncols=2)
    # plot variance-attribute picture
    plt.subplot(1, 2, 1)
    plt.bar(x_attr, y_var, 0.5, align="center", color="c")
    plt.xlabel('Attribute')
    plt.ylabel('Variance')
    plt.xticks(x_attr, var_tick_labels)
    plt.grid()
    # plot ratio-number of attribute picture
    plt.subplot(1, 2, 2)
    plt.bar(x_attr2, y_ratio, 0.5, align="center", color="g")
    plt.xlabel('Number of Attribute')
    plt.ylabel('Cumulative variance ratio')
    plt.xticks(x_attr2, ratio_tick_labels)
    plt.ylim([0, 1])
    plt.grid()
    plt.show()

    # Q1.6 c
    # ﬁrst two principal components
    pc1 = pca.components_[0]
    pc2 = pca.components_[1]
    x0_list = []  # store class0 point
    y0_list = []
    x1_list = []  # store class1 point
    y1_list = []

    for i in range(len(Ytrn)):
        x = productSum(pc1, Xtrn_s[i][:])
        y = productSum(pc2, Xtrn_s[i][:])
        if (Ytrn[i] == 0):
            x0_list.extend([x])
            y0_list.extend([y])
        elif (Ytrn[i] == 1):
            x1_list.extend([x])
            y1_list.extend([y])

    # plot PCA scatter
    plt.scatter(x0_list, y0_list, label='class0', color='blue')
    plt.scatter(x1_list, y1_list, label='class1', color='red')
    plt.xlabel('PC1')
    plt.ylabel('PC2')
    plt.title('PCA Scatter Plot')
    plt.legend(labels=['class0', 'class1'], loc='upper right')
    plt.grid()
    plt.show()

    # Q1.6 d
    pcs = []
    x_axises = []
    corr_coefs = []
    corr_coefs_matrix = []
    pcs.extend([pc1])
    pcs.extend([pc2])
    # generate x1-pc1 x2-pc2
    for i in range(len(pcs)):
        x_axis = []
        for j in range(len(Xtrn_s)):
            x = productSum(pcs[i], Xtrn_s[j])
            x_axis.extend([x])
        x_axises.extend([x_axis])

    # generate correlation coefficient matrix
    for i in range(len(pcs)):
        corr_coef = []
        for j in range(Xtrn_s.shape[1]):
            coef = np.corrcoef(x_axises[i], [x[j] for x in Xtrn_s])
            corr_coef.extend([coef])
        corr_coefs_matrix.extend([corr_coef])

    # generate correlation coefficient
    for line in corr_coefs_matrix:
        corr = []
        for i in range(len(line)):
            corr.extend([line[i][0][1]])
        corr_coefs.extend([corr])

    print('corr_coefs: ' + str(corr_coefs))

# iaml212cw2_q1_6()


# Q1.7
# def iaml212cw2_q1_7():
#
# iaml212cw2_q1_7()   # comment this out when you run the function


# Q1.8
def iaml212cw2_q1_8():
    # rbf_svc = SVC(kernel='rbf', C=0.01)
    skf = StratifiedKFold(n_splits=5)
    train_indexes = []; test_indexes = []
    X_trains = []; X_tests = []; y_trains = []; y_tests = []
    C_list = []
    acc_training = []; acc_testing = []
    mean_train = []; mean_test = []; std_train = []; std_test = []

    # generate C list
    logC_list = np.linspace(-2, 2, 13, endpoint=True)
    for i in range(len(logC_list)):
        C = math.pow(10, logC_list[i])
        C_list.extend([C])

    # generate 5 training datasets and 5 testing datasets
    for train_index, test_index in skf.split(Xtrn_s, Ytrn):
        train_indexes.extend([train_index])
        test_indexes.extend([test_index])
    for i in range(len(test_indexes)):
        X_trains.extend([Xtrn_s[train_indexes[i]]])
        X_tests.extend([Xtrn_s[test_indexes[i]]])
        y_trains.extend([Ytrn[train_indexes[i]]])
        y_tests.extend([Ytrn[test_indexes[i]]])

    # get cross-validation accuracy list for each C
    for i in range(len(C_list)):
        acc_train_list = []
        acc_test_list = []
        for j in range(len(test_indexes)):
            rbf_svc = SVC(kernel='rbf', C=C_list[i])
            rbf_svc.fit(X_trains[j], y_trains[j])
            acc_train_list.extend([rbf_svc.score(X_trains[j], y_trains[j])])
            acc_test_list.extend([rbf_svc.score(X_tests[j], y_tests[j])])
        acc_training.extend([acc_train_list])
        acc_testing.extend([acc_test_list])

    # get the mean and standard deviation of cross-validation accuracy for train and test
    for i in range(len(acc_training)):
        mean1 = np.mean(acc_training[i])
        std1 = np.std(acc_training[i])
        mean2 = np.mean(acc_testing[i])
        std2 = np.std(acc_testing[i])
        mean_train.extend([mean1])
        std_train.extend([std1])
        mean_test.extend([mean2])
        std_test.extend([std2])

    # plot cross validation accuracy-logC
    plt.errorbar(logC_list, mean_train, yerr=std_train, fmt='bo:', color='c', ecolor='r', label='training set')
    plt.errorbar(logC_list, mean_test, yerr=std_test, fmt='bo:', color='b', ecolor='g', label='testing set')
    plt.xlabel('logC')
    plt.ylabel('classification accuracy')
    plt.title('Cross-validation for training and testing set')
    plt.legend()
    plt.show()

    # Q1.8 c
    mean_highest = 0
    acc_highest_C = 0.01

    for i in range(len(mean_test)):
        if (mean_test[i] >= mean_highest):
            mean_highest = mean_test[i]
            acc_highest_C = C_list[i]

    print('the highest mean cross-validation accuracy: ' +str(mean_highest))
    print('the value of C: ' + str(acc_highest_C))

    # Q1.8 d
    best_svc = SVC(kernel='rbf', C=acc_highest_C)
    best_svc.fit(Xtrn_s, Ytrn)
    acc_bestC = best_svc.score(Xtst_s, Ytst)
    correct_num = 100 * acc_bestC

    print('the number of instances correctly classified: ' + str(correct_num))
    print('classiﬁcation accuracy: ' + str(acc_bestC))

# iaml212cw2_q1_8()


# Q1.9
def iaml212cw2_q1_9():
    # generate Ztrn
    Ztrn = []
    for i in range(len(Ytrn)):
        if (Ytrn[i] == 0 and Xtrn[i][4] >= 1):
            Ztrn.extend([[Xtrn[i][4], Xtrn[i][7]]])

    # Q1.9 a
    mean_vec = []
    for i in range(2):
        mean = np.mean([x[i] for x in Ztrn])
        mean_vec.extend([mean])
    print('mean vector: ' + str(mean_vec))

    cov_mat = np.cov(Ztrn, rowvar=False)
    print('covariance matrix: ' + str(cov_mat))

    # Q1.9 b
    x = [k[0] for k in Ztrn]
    y = [k[1] for k in Ztrn]
    # calculate the point density
    xy = np.vstack([x, y])
    z = gaussian_kde(xy)(xy)

    # Gaussian: gaussian distribution probability function for 2-dimensional data
    X, Y = np.meshgrid(np.linspace(0, 60, 1000), np.linspace(0, 60, 1000))
    Gussian = multivariate_normal(mean=mean_vec, cov=cov_mat)
    d = np.dstack([X, Y])
    Z = Gussian.pdf(d).reshape(1000, 1000)

    # plot
    plt.scatter(x, y, c=z, s=10)
    plt.contour(X, Y, Z)
    plt.xlabel('A4')
    plt.ylabel('A7')
    plt.title('2-D Gussian Distribution')
    plt.grid()
    plt.show()

# iaml212cw2_q1_9()


# Q1.10
def iaml212cw2_q1_10():
    # generate Ztrn
    Ztrn = []
    for i in range(len(Ytrn)):
        if (Ytrn[i] == 0 and Xtrn[i][4] >= 1):
            Ztrn.extend([[Xtrn[i][4], Xtrn[i][7]]])

    # Q1.10 a
    mean_vec = []
    for i in range(2):
        mean = np.mean([x[i] for x in Ztrn])
        mean_vec.extend([mean])
    print('mean vector: ' + str(mean_vec))

    cov_mat = np.cov(Ztrn, rowvar=False)
    cov_mat[0][1] = 0; cov_mat[1][0] = 0
    print('covariance matrix: ' + str(cov_mat))

    # Q1.10 b
    x = [k[0] for k in Ztrn]
    y = [k[1] for k in Ztrn]
    # calculate the point density
    xy = np.vstack([x, y])
    z = gaussian_kde(xy)(xy)

    # Gaussian: gaussian distribution probability function for 2-dimensional data
    X, Y = np.meshgrid(np.linspace(0, 60, 1000), np.linspace(0, 60, 1000))
    Gussian = multivariate_normal(mean=mean_vec, cov=cov_mat)
    d = np.dstack([X, Y])
    Z = Gussian.pdf(d).reshape(1000, 1000)

    # plot
    plt.scatter(x, y, c=z, s=10)
    plt.contour(X, Y, Z)
    plt.xlabel('A4')
    plt.ylabel('A7')
    plt.title('2-D Gussian Distribution')
    plt.grid()
    plt.show()


iaml212cw2_q1_10()


# Q1.11
def iaml212cw2_q1_11():
    # set up StratifiedKFold
    skf = StratifiedKFold(n_splits=5)
    train_indexes = []; test_indexes = []
    X_trains = []; X_tests = []; y_trains = []; y_tests = []

    # generate 5 training datasets and 5 testing datasets
    for train_index, test_index in skf.split(Xtrn_s, Ytrn):
        train_indexes.extend([train_index])
        test_indexes.extend([test_index])
    for i in range(len(test_indexes)):
        X_trains.extend([Xtrn_s[train_indexes[i]]])
        X_tests.extend([Xtrn_s[test_indexes[i]]])
        y_trains.extend([Ytrn[train_indexes[i]]])
        y_tests.extend([Ytrn[test_indexes[i]]])

    # get cross-validation accuracy list
    acc_train_list = []
    acc_test_list = []
    for j in range(len(test_indexes)):
        lr = LogisticRegression(max_iter=1000, random_state=0)
        lr.fit(X_trains[j], y_trains[j])
        acc_train_list.extend([lr.score(X_trains[j], y_trains[j])])
        acc_test_list.extend([lr.score(X_tests[j], y_tests[j])])

    # calculate mean and standard deviation
    mean_test = np.mean(acc_test_list)
    std_test = np.std(acc_test_list)
    print('mean of cross-validation accuracy: ' + str(mean_test))
    print('standard deviation of cross-validation accuracy: ' + str(std_test))

    # Q1.11 b
    df = pd.DataFrame({'A0': [x[0] for x in Xtrn_s], 'A1': [x[1] for x in Xtrn_s], 'A2': [x[2] for x in Xtrn_s],
                       'A3': [x[3] for x in Xtrn_s], 'A4': [x[4] for x in Xtrn_s], 'A5': [x[5] for x in Xtrn_s],
                       'A6': [x[6] for x in Xtrn_s], 'A7': [x[7] for x in Xtrn_s], 'A8': [x[8] for x in Xtrn_s]})

    # generate 9 training set
    training_set = []
    for i in range(9):
        s = 'A' + str(i)
        drop_set = df.drop([s], axis=1)
        training_set.extend([drop_set])

    # generate mean and standard deviation for each choice of attribute to drop
    mean_list = []
    std_list = []
    for i in range(9):
        train_indexes = []; test_indexes = []
        X_trains = []; X_tests = []; y_trains = []; y_tests = []

        dataset = training_set[i].values
        # generate 5 training datasets and 5 testing datasets
        for train_index, test_index in skf.split(dataset, Ytrn):
            train_indexes.extend([train_index])
            test_indexes.extend([test_index])
        for m in range(len(test_indexes)):
            X_trains.extend([dataset[train_indexes[m]]])
            X_tests.extend([dataset[test_indexes[m]]])
            y_trains.extend([Ytrn[train_indexes[m]]])
            y_tests.extend([Ytrn[test_indexes[m]]])

        # get cross-validation accuracy list
        acc_train_list = []; acc_test_list = []
        for j in range(len(test_indexes)):
            lr = LogisticRegression(max_iter=1000, random_state=0)
            lr.fit(X_trains[j], y_trains[j])
            acc_train_list.extend([lr.score(X_trains[j], y_trains[j])])
            acc_test_list.extend([lr.score(X_tests[j], y_tests[j])])

        # calculate mean and standard deviation
        mean_test = np.mean(acc_test_list)
        std_test = np.std(acc_test_list)

        # mean and standard deviation for each choice of attribute to drop
        mean_list.extend([mean_test])
        std_list.extend([std_test])

    print('mean_list: ' + str(mean_list))
    print('std_list: ' + str(std_list))

# iaml212cw2_q1_11()
