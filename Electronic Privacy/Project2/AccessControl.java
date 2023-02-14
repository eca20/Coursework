
import java.util.*;

public class AccessControl{

	StringBuilder[][] accessControlMatrix;
	ArrayList<String> userNames;
	ArrayList<String> fileNames;
	ArrayList<String> groupNames;
	int userCount;
	int fileCount;


	private class File{
		public String filename;
		public int indexJ;

		public File(String fname, int j){
			filename = fname;
			indexJ = j;
		}
	}

	private class User{

		public String username;
		public int indexI;

		public User(String uname, int i){
			username = uname;
			indexI = i;
		}

	}


	public AccessControl(){

		accessControlMatrix = new StringBuilder[1000][1000];

		userNames = new ArrayList<String>();
		fileNames = new ArrayList<String>();

		userCount = 0;
		fileCount = 0;

	}

	public void addGroup(String groupName){

	}

	public void addUserToGroup(String userName, String groupName){

	}

	public void addPermToGroup(String groupName, String fileName){

	}

	public boolean addPermToUser(String userName, String fileName, String perm){
			int i = -1;
			int j = -1;

			i = addUser(userName);
			j = addFile(fileName);

			StringBuilder temp = new StringBuilder("");

			if(accessControlMatrix[i][j] == null){
				accessControlMatrix[i][j] = new StringBuilder(perm);
			}
			else{
				temp = accessControlMatrix[i][j];
				temp = createPermissionString(temp, perm);
				accessControlMatrix[i][j] = temp;
				System.out.println("2");
			}

			/*
			for(String s: userNames){
				if(s.equals(userName)){
					for(String f: fileNames){
						if(f.equals(fileName)){
							//username and filename exist
							StringBuilder temp = new StringBuilder("");
							temp = accessControlMatrix[i][j];
							temp = createPermissionString(temp, perm);
							accessControlMatrix[i][j] = temp;
							System.out.println("1");
							return true;
						}
						else{
							//username exists but file does not
							StringBuilder temp = new StringBuilder("");
							temp = accessControlMatrix[i][j];
							temp = createPermissionString(temp, perm);
							accessControlMatrix[i][j] = temp;
							System.out.println("2");
							return true;
						}
					}
				}
				else{
					//need new user
					for(String f: fileNames){
						if(f.equals(fileName)){
							//new user created at x, file exists
							StringBuilder temp = new StringBuilder("");
							temp = accessControlMatrix[i][j];
							temp = createPermissionString(temp, perm);
							accessControlMatrix[i][j] = temp;
							System.out.println("3");
							return true;
						}
						else{
							//new user at x, need new file at y
							StringBuilder temp = new StringBuilder("");
							temp = accessControlMatrix[i][j];
							temp = createPermissionString(temp, perm);
							accessControlMatrix[i][j] = temp;
							System.out.println("4");
							return true;
						}
					}
				}
			}

			*/
			return false;
	}

	public int addUser(String s){
		if(userNames.contains(s)){
			return userNames.indexOf(s);
		}
		else{
			userNames.add(userCount, s);
			userCount++;
			return userCount - 1;
		}
	}

	public int addFile(String s){
		if(fileNames.contains(s)){
			return fileNames.indexOf(s);
		}
		else{
			fileNames.add(fileCount, s);
			fileCount++;
			return fileCount - 1;
		}
	}

	public StringBuilder createPermissionString(StringBuilder current, String toAdd){
		if(current.indexOf(toAdd) == -1){
			return current.append(toAdd);
		}
		else{
			return current;
		}
	}

	public boolean queryAC(String username, String filename, String permission){
		if(!fileNames.contains(filename) || !userNames.contains(username)){
			System.out.println("Username or filename does not exist.");
			return false;
		}
		else{
			int i;
			int j;

			for(String s: userNames){
				i = addUser(s);
				for(String f: fileNames){
					j = addFile(f);
					if(accessControlMatrix[i][j].indexOf(permission) != -1){
						return true;
					} 
					else{
						return false;
					}
				}		
			}

		}
		return false;
	
	}

	public void printAC(){
			int i;
			int j;
			for(String s: userNames){
					i = addUser(s);
					for(String f: fileNames){
							j = addFile(f);
						System.out.println("User " + s + " has permission " + accessControlMatrix[i][j] + " on file " + f + ".");
					}
				
			}
	}


}