tests = """Q(x, y) :- R(x, y)
Q(x, y) :- R(x, y)
PRINT(str) :- USER(id, 'Administer'), INPUT(id, 'try weary in wordle'), INTPUT(id, str)
PRINT(str) :- USER(id, 'Administer'), INPUT(id, 'try weary in wordle'), INTPUT(id, str)
Q(x) :- R(x, y), T(y, 'ADBS'), R(x, 4)
Q(x) :- R(x, 4), T(y, 'ADBS'), R(y, 4)
Q(x, y) :- R(x, y), T(y, z), S(z, x)
Q(x, y) :- R(x, y), T(y, z), S(z, x)
Q() :- R(y, z), R(z, x)
Q() :- R(y, z), R(z, x)
Q() :- R(x, 1), S(y, x), R(u, w)
Q() :- R(x, 1), S(y, x)
OUTPUT(name) :- PAYMENT(rid, amount), CUSTOMER(name, rid), DATE(date, 'some day'), DATE(date, random)
OUTPUT(name) :- PAYMENT(rid, amount), CUSTOMER(name, rid), DATE(date, 'some day')
STUDENTNAME(name) :- STUDENT(sid, name, 'INF'), ENROLLMENT(sid, cid), GRADE(cid, uid, semester, mark), ENROLLMENT(sid, 'ADBS'), GRADE('ADBS', x, 'MMXXII', g)
STUDENTNAME(name) :- STUDENT(sid, name, 'INF'), ENROLLMENT(sid, 'ADBS'), GRADE('ADBS', x, 'MMXXII', g)
Q(x, z) :- R(4, z), S(x, y), T(12, 'x'), R(u, z), R(u, z), S(x, 4), T(14, 'y')
Q(x, z) :- R(4, z), T(12, 'x'), S(x, 4), T(14, 'y')"""

start_num = 4

test_lines = tests.split('\n')
for i in range(len(test_lines) // 2):
    with open("input/query" + str(start_num + i) + '.txt', 'w') as f:
        f.write(test_lines[i*2])
    with open("expected_output/query" + str(start_num + i) + '.txt', 'w') as f:
        f.write(test_lines[i*2 + 1])