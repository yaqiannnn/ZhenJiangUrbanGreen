<%@ WebService Language="C#" Class="GLService" %>


using System;
using System.Linq;
using System.Web;
using System.Web.Services;
using System.Web.Services.Protocols;
using System.IO;
using Newtonsoft.Json;
using Newtonsoft.Json.Converters;
using System.Data;
using System.Transactions;
using SJF.WebBOS;
using SJF.WebBOS.TermiteControl.Cases;
using System.Collections.Generic;
using System.Data.SqlClient;
using SJF.WebBOS.UI.WebGIS;
using SJF.Web.UI.OpenMap;

/// <summary>
/// 提供基本的WebService服务
/// </summary>
[WebService(Namespace = "http://services.ui.webbos.sjf.org/", Description = "常用基本服务")]
[WebServiceBinding(ConformsTo = WsiProfiles.BasicProfile1_1)]
// 若要允许使用 ASP.NET AJAX 从脚本中调用此 Web 服务，请取消注释以下行。 
[System.Web.Script.Services.ScriptService]
public class GLService : SJF.WebBOS.GreenLand.Service.GLServiceBase
{
    /// <summary>
    /// 匿名请求服务
    /// </summary>
    /// <param name="RequestInfo">
    /// JSON字符串, 含有拟调用方法名称和调用参数.
    /// 该字符串经由GZip方法压缩，并转换为Base64字符串
    /// 'wmn'：方法名称，必须以“NoUser_”为前缀；
    /// 'wmp'：方法参数（也是一个JSON字符串）
    /// </param>
    /// <returns></returns>
    ///[WebMethod]
    ///public string RequestServicesWithoutUserInfo(string RequestInfo)

    /// <summary>
    /// 登录请求服务
    /// </summary>
    /// <param name="RequestInfo">
    ///   JSON字符串,含有用户名称，密码，拟调用方法名称，调用参数.
    ///   该字符串经由GZip方法压缩，并转换为Base64字符串
    ///   'lu'：用户名；'lp'：密码；'wmn'：方法名称；'wmp'：方法参数（也是一个JSON字符串）
    /// </param>
    /// <returns></returns>
    ///[WebMethod]
    ///public string RequestServices(string RequestInfo)
    
    /// <summary>
    /// 返回手机App最新版安装包文件的信息：Name，Size，Time，Ver
    /// </summary>
    /// <returns></returns>
    ///public string NoUser_GetCurrentAPKFileInfo()
    
    /// <summary>
    /// 查询某个模块的操作权限
    /// 参数如下
    /// 模块ID：ModuleID
    /// 子功能ID：FunID
    /// 对象分类ID：ObjTypeID，可以为空
    /// </summary>
    /// <returns>
    /// {
    ///     AuthResult: 0 或 1 
    /// }
    /// </returns>
    ///public string QueryModuleFunRight()
            
    /// <summary>
    /// 方法示例
    /// </summary>
    /// <returns></returns>
    public string DemoOfMethod()
    {
        //获取参数，Input
        string parameter1 = GetParameter("ParameterName1");
        string parameter2 = GetParameter("ParameterName2");
        
        //处理过程，Process
        //For Example 
        bool succeed = true;

        //返回值包装，Output
        //Wrap Return
        if (succeed) //成功
        {
            Dictionary<string, object> SucceedReturn = new Dictionary<string, object>();
            SucceedReturn.Add("Result1", "Result1");
            SucceedReturn.Add("Result2", "Result2");
            return SucceedResult(JsonConvert.SerializeObject(SucceedReturn));
        }
        else
        {
            Dictionary<string, object> FailedReturn = new Dictionary<string, object>();
            FailedReturn.Add("Result1", "Result1");
            FailedReturn.Add("Result2", "Result2");
            return FailedMessage("执行失败！", JsonConvert.SerializeObject(FailedReturn));
        }
    }

    const int LATESTVERSION = 2;
    const string APK_URL = "http://192.168.0.106:81/app-release.apk";

    [WebMethod]
    public string NoUser_CheckUpdate()
    {
        int versionCode = int.Parse(GetParameter("VersionCode"));
        
        if (versionCode < LATESTVERSION)
        {
            Dictionary<string, object> results = new Dictionary<string, object>();
            results.Add("version", LATESTVERSION.ToString());
            results.Add("url", APK_URL);
            return SucceedResult(JsonConvert.SerializeObject(results));
        }
        else
        {
            Dictionary<string, object> results = new Dictionary<string, object>();
            return FailedMessage("执行失败！", JsonConvert.SerializeObject(results));
        }
            
    }

    [WebMethod]
    public string CheckUpdate()
    {
        int versionCode = int.Parse(GetParameter("VersionCode"));

        if (versionCode < LATESTVERSION)
        {
            Dictionary<string, object> results = new Dictionary<string, object>();
            results.Add("version", LATESTVERSION.ToString());
            results.Add("url", APK_URL);
            return SucceedResult(JsonConvert.SerializeObject(results));
        }
        else
        {
            Dictionary<string, object> results = new Dictionary<string, object>();
            return FailedMessage("执行失败！", JsonConvert.SerializeObject(results));
        }

    }
    
    [WebMethod]
    public string GetMaintainRecord()
    {
        List<Dictionary<string, object>> results;
        string errorMsg;
        Dictionary<string, object> query = new Dictionary<string, object>();
        int page = int.Parse(GetParameter("page")), limit = int.Parse(GetParameter("limit"));
        if (!GetParameter("submit").Equals(""))
            query.Add("submit", bool.Parse(GetParameter("submit")));
        if (!GetParameter("assess").Equals(""))
            query.Add("assess", bool.Parse(GetParameter("assess")));
        if (!GetParameter("UGO_ID").Equals(""))
            query.Add("UGO_ID", GetParameter("UGO_ID"));
        //int page = 1, limit = 3;
        //query.Add("submit", true);
        //query.Add("assess", true);
        //query.Add("UGO_ID", "001CD9DB-1B7F-400F-A4D7-1A19046753CF");
        results = DataUitls.GetMaintainRecord(page, limit, query, out errorMsg);   
        if(results != null)
        {
            return SucceedResult(JsonConvert.SerializeObject(results));
        }
        else
        {
            return FailedMessage(errorMsg);
        }
    }

    [WebMethod]
    public string GetUGOInfoExceptST()
    {
        string errorMessage;
        List<Dictionary<string, object>> allObjDict = DataUitls.GetUGOInfoExceptST(out errorMessage);

        if (allObjDict != null)
        {
            return SucceedResult(JsonConvert.SerializeObject(allObjDict));
        }
        else
        {
            return FailedMessage(errorMessage);
        }
    }
}


public class DataUitls
{
    public static List<Dictionary<string, object>> GetUGOInfoExceptST(out string errorMsg)
    {
        errorMsg = String.Empty;
        try
        {
            return _GetUGOInfoExceptST();
        }
        catch (Exception e)
        {
            errorMsg = e.Message;
            return null;
        }
    }
    
    public static List<Dictionary<string, object>> GetMaintainRecord(int page, int limit, Dictionary<string, object> query, out string errorMsg)
    {
        errorMsg = String.Empty;
        try
        {
            if (!query.ContainsKey("UGO_ID"))
                return _GetAllMaintainRecord(page, limit, query);
            else
                return _GetUGOMaintainRecord(page, limit, query, query["UGO_ID"].ToString());
        }
        catch (Exception e)
        {
            errorMsg = e.Message;
            return null;
        }
    }
    
    private static List<Dictionary<string, object>> _GetAllMaintainRecord(int page, int limit, Dictionary<string, object> query)
    {
        string queryString = "";
        List<string> queryItems = new List<string>();
        if (query.ContainsKey("assess"))
            queryItems.Add("[MR_AssessStatus] = " + (query["assess"].Equals(true) ? "1" : "0"));
        if (query.ContainsKey("submit"))
            queryItems.Add("[MR_SubmitStatus] = " + (query["submit"].Equals(true) ? "1" : "0"));
        if (queryItems.Count() > 0)
            queryString = "WHERE " + string.Join(" and ", queryItems);
        
        string sqlString = "SELECT TOP " + limit.ToString() + " [MR_ID]," + "[MR_Code]," +
            "[MR_CompanyID]," + "[MR_MaintainType]," + "[MR_MaintainStaff]," + "[MR_MaintainDate]," +
            "[MR_MaintainContent]," + "[MR_LoggerPID]," + "[MR_LogTime]," + "[MR_LastEditorPID] " +
            "From (SELECT row_number() over (order by [MR_MaintainDate] desc) as rownumber,* From [UG_MaintainRecord] " + 
            queryString + ") as a " +
            "WHERE rownumber > " + (page * limit).ToString();
        return _SelectDataFromDB(sqlString);
    }
    
    private static List<Dictionary<string, object>> _GetUGOMaintainRecord(int page, int limit, Dictionary<string, object> query, string UGO_ID)
    {
        string queryString = "";
        List<string> queryItems = new List<string>();
        if (query.ContainsKey("assess"))
            queryItems.Add("[MR_AssessStatus] = " + (query["assess"].Equals(true) ? "1" : "0"));
        if (query.ContainsKey("submit"))
            queryItems.Add("[MR_SubmitStatus] = " + (query["submit"].Equals(true) ? "1" : "0"));
        if (queryItems.Count() > 0)
            queryString = "WHERE " + string.Join(" and ", queryItems);

        string sqlString = "SELECT TOP " + limit.ToString() + " [MR_ID],[MR_Code],[MR_CompanyID],[MR_MaintainType]," +
            "[MR_MaintainStaff],[MR_MaintainDate],[MR_MaintainContent],[MR_LoggerPID],[MR_LogTime],[MR_LastEditorPID] FROM " +
            "(SELECT row_number() over (order by [MR_MaintainDate] desc) as rownumber,* FROM " +
            "(SELECT MR.* FROM [UG_MaintainRecord_Objects] AS Obj JOIN [UG_MaintainRecord] AS MR " +
            "ON Obj.MR_ID = MR.MR_ID and Obj.UGO_ID = \'" + UGO_ID + "\') AS Res " + queryString + ") AS a " + 
            "WHERE rownumber > " + (page * limit).ToString();
        return _SelectDataFromDB(sqlString);
    }
    
    private static List<Dictionary<string, object>> _GetUGOInfoExceptST()
    {
        string GREENLAND_TYPE = "000";
        string ANCIENTTRE_TYPE = "001";
        string sqlString = "SELECT [UGO_ID]" +
              ",[ReferenceSrcObj_ID]" +
              ",[UGO_Ucode]" +
              ",[FlowNo_In_CodeGroup]" +
              ",[UGO_Parent_ID]" +
              ",[UGO_ClassType_ID]" +
              ",[UGO_Name]" +
              ",[UGO_Name_PinYinAbbrevation]" +
              ",[UGO_Geo_Location]" +
              ",[UGO_Address]" +
              ",[UGO_CurrentArea]" +
              ",[UGO_CurrentOwner]" +
              ",[UGO_CurrentOwnerType]" +
              ",[UGO_SpatialDescription]" +
              ",[UGO_Description]" +
              ",[UGO_LoggerPID]" +
              ",[UGO_LogTime]" +
              ",[UGO_LastEditorPID]" +
              ",[UGO_LastEditTime]" +
              ",[UGO_Destroyed]" +
              ",[UGO_DateOfDestroyed]" +
              ",[UGO_DateOfDestroyRecord]" +
              ",[UGO_DescriptionOfDestroy]" +
              ",[UGO_LoggerPIDOfDestroyRecord]" +
              "From [UG_GreenObjects]";
        //"Where [UG_GreenObjects].UGO_ClassType_ID = '" + GREENLAND_TYPE.Replace("'","''") + "'" +
        //"OR [UG_GreenObjects].UGO_ClassType_ID = '" + ANCIENTTRE_TYPE.Replace("'","''") + "'";
        return _SelectDataFromDB(sqlString);
    }

    /// <summary>
    /// 从数据库中选择数据
    /// </summary>
    /// <param name="sql">对应的单条sql语句</param>
    /// <returns>没有数据时，返回null</returns>
    private static List<Dictionary<string, object>> _SelectDataFromDB(string sql)
    {
        SqlCommand cmd = WebBOSDBHelper.GetExecutableCommand();
        cmd.CommandText = sql;
        try
        {
            DataSet res_set = WebBOSDBHelper.ExecuteSQLAndReturnDataSet(cmd);
            if (res_set.Tables.Count == 0 || res_set.Tables[0].Rows.Count == 0)
                return null;
            List<Dictionary<string, object>> results = new List<Dictionary<string, object>>();
            foreach (DataRow row in res_set.Tables[0].Rows)
            {
                Dictionary<string, object> obj = new Dictionary<string, object>();
                foreach (DataColumn key in res_set.Tables[0].Columns)
                {
                    if (key.ToString() == "UGO_Geo_Location")
                    {
                        String geoJson = WKTToGeoJSON.ConvertWKTGeometryToGeoJSON(WKTHelper.FromWKT(row[key].ToString()));
                        obj.Add(key.ToString(), geoJson);
                    }
                    else
                    {
                        obj.Add(key.ToString(), row[key]);
                    }
                }
                results.Add(obj);
            }
            return results;
        }
        catch
        {
            return null;
        }
        finally
        {
            WebBOSDBHelper.CloseConnection(cmd);
        }
    }
    
    private static void _InsertDataIntoDB(string sql)
    {
        
    }

    //#region 通用方法扩展DBHelper
    ///// <summary>
    ///// 从reader中获取dictionary，如果reader一条记录都没有就抛出异常
    ///// </summary>
    ///// <param name="reader"></param>
    ///// <returns></returns>
    //private static Dictionary<string, object> getDictionaryFromReader(SqlDataReader reader)
    //{
    //    Dictionary<string, object> result = new Dictionary<string, object>();
    //    for (int i = 0; i < reader.FieldCount; i++)
    //    {
    //        string fieldName = reader.GetName(i);
    //        object value = reader.GetValue(i);

    //        if (value == DBNull.Value)
    //            result.Add(fieldName, null);
    //        else
    //            result.Add(fieldName, value);
    //    }
    //    return result;

    //}
    //#endregion
}
