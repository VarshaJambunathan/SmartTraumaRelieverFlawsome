# Importing the libraries
import pandas as pd

# Importing the dataset
dataset = pd.read_csv('/home/Websiteyo/mysite/sheet1.tsv', delimiter = '\t', quoting = 3)
laws = ["300. Murder", "304B. Dowry Death", "354A. Sexual harassment and punishment for sexual harassment", "326A. Voluntarily causing grievous hurt by use of acid, etc.", "336. Act endangering life or personal safety of others",
        "350. Criminal force", "354. Assault or criminal force to woman with intent to outrage her modesty", "354D. Stalking", "366. Kidnapping, abducting or inducing woman to compel her marriage, etc",
        "493. Cohabitation caused by a man deceitfully inducing a belief of lawful marriage", "494. Marrying again during lifetime of husband or wife", "498A. Husband or relative of husband of a woman subjecting her to cruelty",
        "Maternity Benefits Act 1961", "Equal Renumeration Act, 1976", "509. Word, gesture or act intended to insult the modesty of a woman", "The Indecent Representation of Women (Prohibition) Act, 1986"]

# Cleaning the texts
import re
#nltk.download('stopwords')
from nltk.corpus import stopwords
from nltk.stem.porter import PorterStemmer

X = dataset.iloc[:, 0].values
y = dataset.iloc[:, 1].values
X_a=[]
y_a=[]
y_h=[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]
for i in range(0,101):
    row = X[i]
    if X[i-1]!=X[i]:
        X_a.append(X[i])
        y_h=[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]
    else:
        y_a.pop()
    if y[i] == "304B. Dowry Death":
        y_h[1]=1
    elif y[i] == "354A. Sexual harassment and punishment for sexual harassment":
        y_h[2]=1
    elif y[i] == "326A. Voluntarily causing grievous hurt by use of acid, etc.":
        y_h[3]=1
    elif y[i] == "336. Act endangering life or personal safety of others":
        y_h[4]=1
    elif y[i] == "350. Criminal force":
        y_h[5]=1
    elif y[i] == "354. Assault or criminal force to woman with intent to outrage her modesty":
        y_h[6]=1
    elif y[i] == "354D. Stalking":
        y_h[7]=1
    elif y[i] == "366. Kidnapping, abducting or inducing woman to compel her marriage, etc":
        y_h[8]=1
    elif y[i] == "493. Cohabitation caused by a man deceitfully inducing a belief of lawful marriage":
        y_h[9]=1
    elif y[i] == "494. Marrying again during lifetime of husband or wife":
        y_h[10]=1
    elif y[i] == "498A. Husband or relative of husband of a woman subjecting her to cruelty":
        y_h[11]=1
    elif y[i] == "Maternity Benefits Act 1961":
        y_h[12]=1
    elif y[i] == "Equal Renumeration Act, 1976":
        y_h[13]=1
    elif y[i] == "509. Word, gesture or act intended to insult the modesty of a woman":
        y_h[14]=1
    elif y[i] == "The Indecent Representation of Women (Prohibition) Act, 1986":
        y_h[15]=1
    else:
        y_h[0]=1
    y_a.append(y_h)

X_a.append("fefefe")
y_h=[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]
y_a.append(y_h);
X_a.append("")
y_a.append(y_h)
X_a.append("murder")
y_h[0]=1
y_a.append(y_h)

corpus = []
for i in range(0, 63):
    review = re.sub('[^a-zA-Z]', ' ', X_a[i])
    review = review.lower()
    review = review.split()
    ps = PorterStemmer()
    review = [ps.stem(word) for word in review if not word in set(stopwords.words('english'))]
    review = ' '.join(review)
    corpus.append(review)

# Creating the Bag of Words model
from sklearn.feature_extraction.text import CountVectorizer
cv = CountVectorizer(max_features = 1500)
X = cv.fit_transform(corpus).toarray()
y = y_a

# Splitting the dataset into the Training set and Test set
from sklearn.cross_validation import train_test_split
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size = 0.0, random_state = 0)

# Fitting Naive Bayes to the Training set
from skmultilearn.problem_transform import LabelPowerset
from sklearn.naive_bayes import GaussianNB
import json
classifier = LabelPowerset(GaussianNB())
classifier.fit(X_train, y_train)

from flask import Flask, request
app = Flask(__name__)

@app.route('/', methods=['POST'])
def hello():
    input_desc = request.form['ind']
    # Predicting the Test set results
    pred_data1 = []
    review = re.sub('[^a-zA-Z]', ' ', input_desc)
    review = review.lower()
    review = review.split()
    ps = PorterStemmer()
    review = [ps.stem(word) for word in review if not word in set(stopwords.words('english'))]
    review = ' '.join(review)
    pred_data1.append(review)
    pred_data = cv.transform(pred_data1).toarray()
    y_pred = classifier.predict(pred_data)
    y_pred = y_pred.toarray()
    y_pred = y_pred[0]

    #Decoding the Law
    len1 = len(y_pred)
    y_pred1 = []
    for i in range(0, len1):
        if y_pred[i] == 1:
            y_pred1.append(laws[i])
    y_pred = y_pred1
    y_pred = json.dumps(y_pred)
    return y_pred

if __name__ == '__main__':
	app.run()