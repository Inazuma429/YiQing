package servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Dao.CityDao;

/**
 * Servlet implementation class CityServlet
 */
@WebServlet("/CityServlet")
public class CityServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CityServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=UTF-8");
		String date=request.getParameter("dateC");
		//在河北省数据库中，记录日期的格式是X年X月X日，其他数据库格式是X-X-X，因此需要对接收的日期信息进行格式更改
		String dateStr[]=date.split("-");
		char judge1,judge2;
		judge1=dateStr[1].charAt(0);
		judge2=dateStr[2].charAt(0);
		if(judge1=='0') {
			dateStr[1]=dateStr[1].substring(1);
		}
		if(judge2=='0') {
			dateStr[2]=dateStr[2].substring(1);
		}
		String finaldate=dateStr[0]+"年"+dateStr[1]+"月"+dateStr[2]+"日";
		response.getWriter().write(CityDao.searchCity(finaldate));
	}

}
