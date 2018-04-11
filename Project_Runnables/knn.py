"""
    File name: knn.py
    Author: Abhishek Manoj Sharma, Nivetha Vijayaraju, Parth Jayantilal Jain
    Date last modified: 12/04/2017
    Python Version: 3.6.3
"""

def computeKNN(list_of_attr):
    """
    computeKNN() method accepts a percept sequence and returns the predicted class value (heart risk factor).
    :param list_of_attr: Accepts a percept sequence as list_of_attr to predict a class for.
    :return: Returns the predicted class label for the percept.
    """
    from sklearn.neighbors import KNeighborsClassifier
    training_file = "heart_dataset.csv"
    attributes = []
    class_values = []
    with open(training_file) as trainer:
        for line in trainer:
            att = line.strip().split(",")
            att_temp = []
            for i in range (0,len(att)):
                try:
                    t = float(att[i])
                    if t==-9.0:
                        att_temp.append(float(0.0))
                    else:
                        att_temp.append(float(att[i]))
                except ValueError:
                    att_temp.append(float(0.0))
            attributes.append(att_temp[0:21])
            class_values.append(att_temp[21])
    k_neighbors = KNeighborsClassifier(n_neighbors=5)
    k_neighbors.fit(attributes, class_values)
    prediction = k_neighbors.predict(list_of_attr)
    print ("Prediction",prediction[0],end=' - ')
    return prediction[0]