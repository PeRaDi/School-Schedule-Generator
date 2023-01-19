import pandas as pd
import yfinance as finance
import numpy
from autots import AutoTS
import datetime
from datetime import date, timedelta

dateTime = date.today()
d1 = dateTime.strftime("%Y-%m-%d")
endDate = d1

# analise de 1 ano e meio
initialDate = date.today() - timedelta(days=548)
initialDate = initialDate.strftime("%Y-%m-%d")

data = finance.download('BTC-EUR', start=initialDate, end=endDate, progress=False)
#print(data)

data["Date"] = data.index
data = data[["Date", "Open", "High", "Low", "Close", "Adj Close", "Volume"]]
data.reset_index(drop=True, inplace=True)
print(data.head())

#previsao 10 dias, infer -> timestamps
model = AutoTS(forecast_length=10, frequency='infer', ensemble='simple')
model = model.fit(data, date_col='Date', value_col='Close', id_col=None)
prediction = model.predict()

print(prediction.forecast)

