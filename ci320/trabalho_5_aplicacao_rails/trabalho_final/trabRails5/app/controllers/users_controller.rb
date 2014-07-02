class UsersController < ApplicationController
	def new
		@user = User.new
	end

	def create
		@user = User.new(user_params)
		if @user.login == 'admin'
			@user.admin = true
		else
			@user.admin = false
		end
		if @user.save
			redirect_to root_url, :notice => "Cadastrado!"
		else
		render "new"
		end
	end
		
def user_params
  params.require(:user).permit(:login, :password, :password_confirmation)
end
end
