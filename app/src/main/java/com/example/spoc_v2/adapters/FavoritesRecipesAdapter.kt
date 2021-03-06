package com.example.spoc_v2.adapters

import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.spoc_v2.R
import com.example.spoc_v2.data.database.entities.FavoritesEntity
import com.example.spoc_v2.databinding.FavoriteRecipesRowLayoutBinding
import com.example.spoc_v2.ui.fragments.favorites.FavoriteRecipesFragmentDirections
import com.example.spoc_v2.util.RecipesDiffUtil
import com.example.spoc_v2.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.favorite_recipes_row_layout.view.*


class FavoritesRecipesAdapter(
    private val requireActivity: FragmentActivity,
    private val mainViewModel: MainViewModel
): RecyclerView.Adapter<FavoritesRecipesAdapter.MyViewHolder>(), ActionMode.Callback {

    private var multiselection = false
    private lateinit var mActionMode: ActionMode
    private lateinit var rootView: View
    private var selectedRecipes = arrayListOf<FavoritesEntity>()
    private var myViewHolders = arrayListOf<MyViewHolder>()

    private var favoriteRecipes = emptyList<FavoritesEntity>()
    class MyViewHolder(private val binding: FavoriteRecipesRowLayoutBinding):
        RecyclerView.ViewHolder(binding.root) {

            fun bind(favoritesEntity: FavoritesEntity){
                binding.favoritesEntity = favoritesEntity
                binding.executePendingBindings()
            }
        companion object{
            fun from(parent: ViewGroup): MyViewHolder{

                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FavoriteRecipesRowLayoutBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }

    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        myViewHolders.add(holder)
        rootView = holder.itemView.rootView
        val currentRecipe = favoriteRecipes[position]
        holder.bind(currentRecipe)

        /**
         * Single Click Listener
         */
        holder.itemView.favoriteRecipesRowLayout.setOnClickListener{
            if(multiselection){
                applySelection(holder, currentRecipe)
            }else{
                val action =
                    FavoriteRecipesFragmentDirections.actionFavoriteRecipesFragment2ToDetailsActivity(
                        currentRecipe.result)
                holder.itemView.findNavController().navigate(action)
            }
        }

        /**
         * Long Click Listener
         */
        holder.itemView.favoriteRecipesRowLayout.setOnLongClickListener{
            if(!multiselection){
                multiselection = true
                requireActivity.startActionMode(this)
                applySelection(holder, currentRecipe)
                true
            }else{
                applySelection(holder, currentRecipe)
                true
            }
        }
    }

    private fun applySelection(holder: MyViewHolder, currentRecipe: FavoritesEntity){
        if(selectedRecipes.contains(currentRecipe)){
            selectedRecipes.remove(currentRecipe)
            changeRecipeStyle(holder, R.color.cardBackgroundColor, R.color.strokeColor)
            applyActionModeTitle()
        }else{
            selectedRecipes.add(currentRecipe)
            changeRecipeStyle(holder, R.color.cardBackgroundLightColor, R.color.colorPrimary)
            applyActionModeTitle()
        }
    }
    private fun changeRecipeStyle(holder: MyViewHolder, backgroundColor: Int, strokeColor: Int){
        holder.itemView.favoriteRecipesRowLayout.setBackgroundColor(
            ContextCompat.getColor(requireActivity, backgroundColor)
        )
        holder.itemView.favorite_row_cardView.strokeColor =
            ContextCompat.getColor(requireActivity, strokeColor)
    }

    private fun applyActionModeTitle(){
        when(selectedRecipes.size){
            0 -> {
                mActionMode.finish()
                multiselection = false
            }
            1 ->{
                mActionMode.title = "${selectedRecipes.size} item selected"
            }
            else ->{
                mActionMode.title = "${selectedRecipes.size} item selected"
            }

        }
    }

    override fun getItemCount(): Int {
        return favoriteRecipes.size
    }


    override fun onCreateActionMode(actionMode: ActionMode?, menu: Menu?): Boolean {
        actionMode?.menuInflater?.inflate(R.menu.favorites_contextual_menu, menu)
        mActionMode = actionMode!!
        applyStatusBarColor(R.color.contextualStatusBarColor)
        return true
    }

    override fun onPrepareActionMode(actionMode: ActionMode?, menu: Menu?): Boolean {
        return true
    }

    override fun onActionItemClicked(actionMode: ActionMode?, menu: MenuItem?): Boolean {
        if(menu?.itemId == R.id.delete_favorite_recipe_menu){
            selectedRecipes.forEach{
                mainViewModel.deleteFavoriteRecipe(it)
            }
            showSnackBar("${selectedRecipes.size} Recipe/s removed.")

            multiselection = false
            selectedRecipes.clear()
            actionMode?.finish()
        }
        return true
    }

    override fun onDestroyActionMode(actionMode: ActionMode?) {
        myViewHolders.forEach{ holder ->
            changeRecipeStyle(holder, R.color.cardBackgroundColor, R.color.strokeColor)
        }
        multiselection = false
        selectedRecipes.clear()
        applyStatusBarColor(R.color.statusBarColor)

    }

    private fun applyStatusBarColor(color: Int){
        requireActivity.window.statusBarColor =
            ContextCompat.getColor(requireActivity, color)

    }
    fun setData(newFavoriteRecipes: List<FavoritesEntity>){
        val favoriteRecipesDiffUtil =
            RecipesDiffUtil(favoriteRecipes, newFavoriteRecipes)
        val diffUtilResult = DiffUtil.calculateDiff(favoriteRecipesDiffUtil)
        favoriteRecipes = newFavoriteRecipes
        diffUtilResult.dispatchUpdatesTo(this)
    }

    private fun showSnackBar(message: String){
        Snackbar.make(
            rootView,
            message,
            Snackbar.LENGTH_SHORT
        ).setAction("Okay"){}
            .show()

    }

    fun clearContextualActionMode(){
        if(this::mActionMode.isInitialized){
            mActionMode.finish()
        }
    }
}