"""
    File name: prediction_server.py
    Author: Abhishek Manoj Sharma, Nivetha Vijayaraju, Parth Jayantilal Jain
    Date last modified: 12/04/2017
    Python Version: 3.6.3
"""

from knn import computeKNN


def getfromFirebase(username):
    """
    getFromFirebase() gets the data for a specific user from Firebase and stores it in a dictionary object.
    If username passed is of type None, then data for all users is downloaded.
    """
    from firebase import firebase
    firebase_url = 'https://androidapp-b5204.firebaseio.com'
    firebase = firebase.FirebaseApplication(firebase_url, None)
    result = firebase.get('/User', username )
    return result


def updateUser(username,prediction):
    """
    updateUser() method accepts the username and prediction value as arguments and updates the same on Firebase
    """
    from firebase import firebase
    firebase_url = 'https://androidapp-b5204.firebaseio.com'
    firebase = firebase.FirebaseApplication(firebase_url, None)
    key_name = "User/" + username
    firebase.put(key_name,"To_Check",0)
    firebase.put(key_name,"Last_Prediction",prediction)
    print ("Update successful")


def updateLocalCSVFile(re):
    """
    updateLocalCSVFile() method accepts a dictionary object created from Firebase as input.
    Using the dictionary object, it creates a CSV file of the data as a cached copy of online data.
    """
    import pandas as pd
    import os
    file_name = "heart_dataset.csv"
    try:
        os.remove(file_name)
    except FileNotFoundError:
        pass
    except PermissionError:
        print ("Local copy could not be created as the file may be open by other application")
        return
    list_of_users = []
    for key, value in re.items():
        current_user = []
        try:
            current_user.append(float(value["Age"]))
            current_user.append(float(value["Gender"]))
            current_user.append(float(value["Chest_Pain_Type"]))
            current_user.append(float(value["Resting_blood_pressure"]))
            current_user.append(float(value["Serum_cholestrol"]))
            current_user.append(float(value["Cigs_per_day"]))
            current_user.append(float(value["No_of_years_as_smoker"]))
            current_user.append(float(value["Fasting_blood_sugar"]))
            current_user.append(float(value["Diabetes_family_history"]))
            current_user.append(float(value["Coronary_Artery_Disease_Family_History"]))
            current_user.append(float(value["Resting_ECG_result"]))
            current_user.append(float(value["Pulse_Rate_Max"]))
            current_user.append(float(value["Resting_pulse_rate"]))
            current_user.append(float(value["Peak_exercise_BP_High"]))
            current_user.append(float(value["Peak_exercise_BP_Low"]))
            current_user.append(float(value["Resting_blood_pressure_low"]))
            current_user.append(float(value["Exercise_induced_angina"]))
            current_user.append(float(value["Depression_induced_exercise"]))
            current_user.append(float(value["Slope_peak_exercise_ST"]))
            current_user.append(float(value["No_of_major_vessels"]))
            current_user.append(float(value["Pulse_Rate"]))
            current_user.append(float(value["Last_Prediction"]))
            current_user = tuple(current_user)
            list_of_users.append(current_user)
        except KeyError:
            print ("User",key,"added to the system")
    df = pd.DataFrame(list_of_users)
    df.to_csv(file_name, index=False, header=False)
    print ("Local copy of dataset created", "Records:",len(list_of_users))


def mainProgram():
    """
    mainProgram() acts as the driver program and drives the entire flow of the program.
    """
    username = None
    re = getfromFirebase(username)
    print ("-------------------------------")
    updateLocalCSVFile(re)
    users_to_calculate = []
    print("Checking active users")
    for key in re.items():
        if (key[1]["To_Check"] == 1):
            users_to_calculate.append(key)
    if len(users_to_calculate) == 0:
        print("No active users found")
    for item in users_to_calculate:
        curr_list = []
        try:
            curr_list.append(float(item[1]["Age"]))
            curr_list.append(float(item[1]["Gender"]))
            curr_list.append(float(item[1]["Chest_Pain_Type"]))
            curr_list.append(float(item[1]["Resting_blood_pressure"]))
            curr_list.append(float(item[1]["Serum_cholestrol"]))
            curr_list.append(float(item[1]["Cigs_per_day"]))
            curr_list.append(float(item[1]["No_of_years_as_smoker"]))
            curr_list.append(float(item[1]["Fasting_blood_sugar"]))
            curr_list.append(float(item[1]["Diabetes_family_history"]))
            curr_list.append(float(item[1]["Coronary_Artery_Disease_Family_History"]))
            curr_list.append(float(item[1]["Resting_ECG_result"]))
            curr_list.append(float(item[1]["Pulse_Rate_Max"]))
            curr_list.append(float(item[1]["Resting_pulse_rate"]))
            curr_list.append(float(item[1]["Peak_exercise_BP_High"]))
            curr_list.append(float(item[1]["Peak_exercise_BP_Low"]))
            curr_list.append(float(item[1]["Resting_blood_pressure_low"]))
            curr_list.append(float(item[1]["Exercise_induced_angina"]))
            curr_list.append(float(item[1]["Depression_induced_exercise"]))
            curr_list.append(float(item[1]["Slope_peak_exercise_ST"]))
            curr_list.append(float(item[1]["No_of_major_vessels"]))
            curr_list.append(float(item[1]["Pulse_Rate"]))
            list_to_send = [curr_list]
            print("Updating", item[0],end=': ')
            predicted_value = computeKNN(list_to_send)
            updateUser(item[0], predicted_value)
        except KeyError:
            pass

if __name__ == "__main__":
    import time
    SLEEP_IN_SECONDS = 5
    while(True):
        mainProgram()
        time.sleep(SLEEP_IN_SECONDS)