class User < ActiveRecord::Base
	validates_confirmation_of :password
	validates_presence_of :password
	validates_presence_of :login
	validates_uniqueness_of :login
	
  def self.authenticate(login, password)
    user = find_by_login(login)
    if user && user.password == password
      user
    else
      nil
    end
  end
end
