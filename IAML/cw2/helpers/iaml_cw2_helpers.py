#
#  Helper functions for IAML 2021/22 S1 cw2
#   by Hiroshi Shimodaira (h.shimodaira@ed.ac.uk)
#
import os
import gzip
import numpy as np
import scipy
from scipy.io import loadmat
import pandas as pd

def print_versions():
    import platform
    import scipy
    import numpy
    import sklearn
    import matplotlib
    import seaborn
    import pandas

    tabs = [ ['Python', platform.python_version(), '3.9.2'],
             ['Scipy', scipy.__version__, '1.7.0'],
             ['Numpy', numpy.__version__, '1.21.1'],
             ['Sklearn', sklearn.__version__, '0.24.2'],
             ['Pandas', pandas.__version__, '1.3.1'],
             ['Matplotlib', matplotlib.__version__, '3.4.2'],
             ['Seaborn', seaborn.__version__, '0.11.1']
            ]
    for p in tabs:
        print('%s\t%s ' % (p[0], p[1]), end='')
        if p[1] == p[2]:
            print(': Ok')
        else:
            print('<=> %s' % p[2])

def load_Q1_dataset(filename='data/dataset_q1.csv'):
    df = pd.read_csv(filename)
    # print(df.columns)
    X = df[df.columns[0:-1]].to_numpy()
    Y = df[df.columns[-1]].to_numpy()
    return(X, Y)

def load_Q2_dataset(filename='data/dataset_q2.mat'):
    data = loadmat(filename);
    Xtrn_org = data['dataset']['train'][0,0]['images'][0,0].astype(dtype=np.float_)
    Xtst_org = data['dataset']['test'][0,0]['images'][0,0][:,:].astype(dtype=np.float_)
    Ytrn_org = data['dataset']['train'][0,0]['labels'][0,0].astype(dtype=np.int_).flatten()
    Ytst_org = data['dataset']['test'][0,0]['labels'][0,0].astype(dtype=np.int_).flatten()

    return(Xtrn_org, Ytrn_org, Xtst_org, Ytst_org)

