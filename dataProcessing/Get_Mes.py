import pymysql
import requests
import re
from bs4 import BeautifulSoup


class Spider(object):    #爬取的信息有秩序，所以键入开始url的最后一个数字字符
    def __init__(self,num):
        self.url_lastnum = num
        self.headers = {"User-Agent": "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.84 Mobile Safari/537.36", 'Connection': 'close'}
        self.db = pymysql.connect(host="134.175.16.143",user="schoolhelp6!",password="Zgdr@Very6!",database="recruitment")
        self.cur= self.db.cursor()

    def __del__(self):
        self.cur.close()
        self.db.close()


    def Spider_Of_Campus(self):     #爬取校招信息
        base_url = 'http://www.fjrclh.com/showMeeting.asp?id='

        while(1):
            error_times = 0
            try:
                now_uri = base_url + str(self.url_lastnum)
                mes = requests.get(now_uri,headers = self.headers,timeout=10)
                mes.raise_for_status()
                mes.encoding=mes.apparent_encoding
                temp = BeautifulSoup(mes.text,'html.parser')
                soup = temp.find_all('div',class_='content1 clearfix')
                string = str(soup)
                title = re.findall(r'(?s)<h1>.+</h1>',string)
                if(title==[]):     #页面不存在，以爬取全部数据
                    error_times+=1
                    if(error_times>=20):      #爬取空界面超过10次，退出
                        break
                else:
                    error_times=0
                    title2 = title[0]
                    title2 = title2.replace(' ','')
                    title2 = title2.replace('\n','')[4:-5]
                    time = re.findall(r'时间：\d{4}/\d{1,2}/\d{1,2}',string)[0][3:]
                sql = "INSERT INTO rec_info(title,is_campus,url,date) VALUES (%s,%s,%s,%s);"
                try:
                    self.cur.execute(sql,[title2,'1',now_uri,time])
                    self.db.commit()
                except Exception as e:
                    self.url_lastnum +=1
                    self.db.rollback()
                self.url_lastnum +=1
            except Exception as e:
                self.url_lastnum += 1
                print(e,self.url_lastnum)
                continue

    def Spider_Of_Network(self):   #爬取网招信息
        base_url = 'http://www.fjrclh.com/newsshownew.asp?articleid='
        error_times = 0
        while (1):
            try:
                now_uri = base_url + str(self.url_lastnum)
                mes = requests.get(now_uri, headers=self.headers, timeout=10)
                mes.raise_for_status()
                mes.encoding = mes.apparent_encoding
                temp = BeautifulSoup(mes.text, 'html.parser')
                soup = temp.find_all('div', class_='content1 clearfix')
                string = str(soup)
                #print(string)
                title = re.findall(r'(?s)<h1>.+</h1>', string)
                if (title == []):  # 页面不存在，以爬取全部数据
                    error_times += 1
                    if (error_times >= 20):  # 爬取空界面超过20次，退出
                        break
                else:
                    error_times = 0
                    time = re.findall(r'时间\s*\d{4}/\d{1,2}/\d{1,2}', string)[0][4:]
                    title2 = title[0]
                    title2 = title2.replace(' ', '')
                    title2 = title2.replace('\n', '')
                    title2=title2[4:-5]
                    print(title2)
                sql = "INSERT INTO test(title,is_campus,url,date) VALUES (%s,%s,%s,%s);"
                try:
                    self.cur.execute(sql, [title2, '0', now_uri, time])
                    self.db.commit()
                except Exception as e:
                    print(e)
                    self.url_lastnum += 1
                    self.db.rollback()
                self.url_lastnum += 1

            except Exception as e:
                self.url_lastnum += 1
                print(e,self.url_lastnum)
                continue



if __name__=='__main__':
    # spider1=Spider(5200)
    # spider1.Spider_Of_Campus()
    spider2=Spider(43000)
    spider2.Spider_Of_Network()







